package com.polymeric.service.admin.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.finance.FinanceAddressDao;
import com.polymeric.dao.finance.FinanceRechargeRecordDao;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsWebHookMsgDao;
import com.polymeric.dao.order.OrderMchCashFlowDao;
import com.polymeric.entity.finance.FinanceAddressEntity;
import com.polymeric.entity.finance.FinanceRechargeRecordEntity;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserEntity;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;
import com.polymeric.entity.order.OrderMchCashFlowEntity;
import com.polymeric.enums.OrderStatusEnum;
import com.polymeric.enums.OrderTypeEnum;
import com.polymeric.enums.TxStatusEnums;
import com.polymeric.enums.UniversalEnums;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.enums.WebHookStateEnum;
import com.polymeric.enums.WebhookPoloTypeEnums;
import com.polymeric.query.webhook.WebhookQuery;
import com.polymeric.service.admin.FinanceService;
import com.polymeric.service.api.impl.ApiMchWebhook;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.OrderCodeFactory;
import com.polymeric.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.polymeric.base.BaseApiService.setResultError;
import static com.polymeric.base.BaseApiService.setResultSuccess;

/**
 * 类描述：财务管理实现
 *
 * @author GeminiSun
 * @date 2026/07/02 14:50
 */
@RestController
@Transactional
@CrossOrigin
@Slf4j
public class FinanceServiceImpl implements FinanceService {

    @Resource
    private FinanceAddressDao financeAddressDao;

    @Resource
    private FinanceRechargeRecordDao financeRechargeRecordDao;

    @Resource
    private MerchantsInfoDao merchantsInfoDao;
    
    @Autowired
    private OrderMchCashFlowDao orderMchCashFlowDao;
    
    @Autowired
    private MerchantsWebHookMsgDao merchantsWebHookMsgDao;

    @Resource
    private TokenUtils tokenUtils;

    @Override
    public ResponseBase financeList() {
        QueryWrapper<FinanceAddressEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("setTime");
        List<FinanceAddressEntity> financeAddressEntities = financeAddressDao.selectList(wrapper);
        return setResultSuccess(financeAddressEntities);
    }

    @Override
    public ResponseBase financeAdd(@RequestBody FinanceAddressEntity entity) throws InvocationTargetException, IllegalAccessException {
        String address = entity.getAddress();
        FinanceAddressEntity financeAddressEntity = financeAddressDao.selectOne(new QueryWrapper<FinanceAddressEntity>().eq("address", address));
        if (financeAddressEntity != null){
            return setResultError("充值地址已存在，切勿重复添加");
        }
        entity.setStatus(UniversalEnums.OPEN.getIndex());
        GenericityUtil.setDate(entity);
        financeAddressDao.insert(entity);
        return setResultSuccess();
    }

    @Override
    public ResponseBase financeUpdate(@RequestBody FinanceAddressEntity entity) {
        entity.setGmtModified(new Date());
        financeAddressDao.updateById(entity);
        return setResultSuccess();
    }

    @Override
    public ResponseBase financeStatus(Integer id, Integer status) {
        FinanceAddressEntity financeAddressEntity = financeAddressDao.selectById(id);
        if (financeAddressEntity == null){
            return setResultError("地址信息不存在");
        }
        financeAddressEntity.setStatus(status);
        financeAddressEntity.setGmtModified(new Date());
        financeAddressDao.updateById(financeAddressEntity);
        return setResultSuccess();
    }

    @Override
    public ResponseBase recordList(@RequestBody FinanceRechargeRecordEntity entity) {
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        if (!tokenUtils.isAdmin()){
            entity.setMerchantId(String.valueOf(tokenUtils.getMerchantId()));
        }
        List<FinanceRechargeRecordEntity> list = financeRechargeRecordDao.selectAll(entity);
        PageInfo<FinanceRechargeRecordEntity> info = new PageInfo<>(list);
        return setResultSuccess(info, Constants.SUCCESS);
    }

    @Override
    public ResponseBase financeCheck(Integer id,Integer status) {
        FinanceRechargeRecordEntity financeRechargeRecordEntity = financeRechargeRecordDao.selectById(id);
        String merchantId = financeRechargeRecordEntity.getMerchantId();
        if (status.equals(TxStatusEnums.SUCCESS.getIndex())){
            // 修改商户信息
            QueryWrapper<MerchantsInfoEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("id",merchantId);
            wrapper.eq("merchants_status", UserStateEnums.NORMAL.getIndex());
            MerchantsInfoEntity merchantsInfoEntity = merchantsInfoDao.selectOne(wrapper);
            if (merchantsInfoEntity == null){
                return setResultError("商户信息不存在，审核失败");
            }
            //修改商户余额
            BigDecimal beforeAmount = merchantsInfoEntity.getAvailableAmount();
            BigDecimal availableAmount = merchantsInfoEntity.getAvailableAmount() == null ? BigDecimal.ZERO : merchantsInfoEntity.getAvailableAmount();
            BigDecimal newAmount = availableAmount.add(financeRechargeRecordEntity.getAmount());
            merchantsInfoEntity.setAvailableAmount(newAmount);
            merchantsInfoEntity.setGmtModified(new Date());
            merchantsInfoDao.updateById(merchantsInfoEntity);
            //新增商户资金明细记录
            OrderMchCashFlowEntity mchCashFlowEntity = this.addTradeList(beforeAmount,merchantsInfoEntity,financeRechargeRecordEntity);
            // 构建商户回调消息
            WebhookQuery entity = new WebhookQuery();
            entity.setEventId(UUID.randomUUID().toString().replace("-", ""));
            entity.setEventType(WebhookPoloTypeEnums.MERCHANT_RECHARGE.getCode());
            entity.setAmount(financeRechargeRecordEntity.getAmount().toString());
            entity.setTxTime(System.currentTimeMillis());
            entity.setCurreny(Constants.USD);
            entity.setTxHash(mchCashFlowEntity.getOrderNum());
    		MerchantsWebHookMsgEntity msgEntity = this.buildMsg(WebhookPoloTypeEnums.MERCHANT_RECHARGE.getIndex(),merchantsInfoEntity, entity);
    		// 回调给商户
    		ApiMchWebhook.callbackMerchants(msgEntity);
        }

        //变更充值记录状态
        financeRechargeRecordEntity.setTxStatus(String.valueOf(status));
        financeRechargeRecordEntity.setGmtModified(new Date());
        financeRechargeRecordDao.updateById(financeRechargeRecordEntity);
        return setResultSuccess();
    }
    
    /**
     * @category 新增商户资金流水
     * @param beforeAmount
     * @param merchantsInfoEntity
     * @param financeRechargeRecordEntity
     */
    public OrderMchCashFlowEntity addTradeList(BigDecimal beforeAmount, MerchantsInfoEntity merchantsInfoEntity, FinanceRechargeRecordEntity financeRechargeRecordEntity) {
    	try {
    		String orderNum = "WP"+OrderCodeFactory.getOrderCode(null);
    		OrderMchCashFlowEntity merchantsOrderEntity = new OrderMchCashFlowEntity();
    		merchantsOrderEntity.setOrderNum(orderNum);
    		merchantsOrderEntity.setMchOrderNum(orderNum);
    		merchantsOrderEntity.setMchId(merchantsInfoEntity.getId());
    		merchantsOrderEntity.setMchAppid(merchantsInfoEntity.getAppId());
    		merchantsOrderEntity.setTradeType(OrderTypeEnum.BALANCE_TOP_UP.getLable());
    		merchantsOrderEntity.setOrderType(OrderTypeEnum.BALANCE_TOP_UP.getCode());
    		merchantsOrderEntity.setOrderAmount(financeRechargeRecordEntity.getAmount());
    		merchantsOrderEntity.setActualAmount(financeRechargeRecordEntity.getAmount());
    		merchantsOrderEntity.setBeforeAmount(beforeAmount);
    		merchantsOrderEntity.setAfterAmount(merchantsInfoEntity.getAvailableAmount());
    		merchantsOrderEntity.setOrderState(OrderStatusEnum.SUCCESS.getCode());
    		GenericityUtil.setDate(merchantsOrderEntity);
    		orderMchCashFlowDao.insert(merchantsOrderEntity);
    		return merchantsOrderEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    }
    
    public <T> MerchantsWebHookMsgEntity buildMsg(Integer msgType,MerchantsInfoEntity infoEntity,T requestBody) {
		try {
			MerchantsWebHookMsgEntity msgEntity = new MerchantsWebHookMsgEntity();
			msgEntity.setMsgCode(OrderCodeFactory.getMsgCode(null));
			msgEntity.setMchAppid(infoEntity.getAppId());
//			msgEntity.setUid(userEntity.getApiUid());
			msgEntity.setMsgType(msgType);
			msgEntity.setMsgTypeName(WebhookPoloTypeEnums.getName(msgType));
			MerchantsInfoEntity merchantsInfoEntity = merchantsInfoDao.findByAppId(infoEntity.getAppId());
			if(merchantsInfoEntity != null) {
				msgEntity.setCallbackUrl(merchantsInfoEntity.getWebhookUrl());			
			}
			msgEntity.setCallbackData(JSON.toJSONString(requestBody));
			msgEntity.setStatus(WebHookStateEnum.PENDING.getCode());
			msgEntity.setRetryCount(Constants.ZERO_INT);
			GenericityUtil.setDate(msgEntity);
			merchantsWebHookMsgDao.insert(msgEntity);
			return msgEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
    
}