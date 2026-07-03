package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.finance.FinanceAddressDao;
import com.polymeric.dao.finance.FinanceRechargeRecordDao;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.entity.finance.FinanceAddressEntity;
import com.polymeric.entity.finance.FinanceRechargeRecordEntity;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.enums.TxStatusEnums;
import com.polymeric.enums.UniversalEnums;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.service.admin.FinanceService;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.Token;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
            BigDecimal availableAmount = merchantsInfoEntity.getAvailableAmount() == null ? BigDecimal.ZERO : merchantsInfoEntity.getAvailableAmount();
            BigDecimal newAmount = availableAmount.add(financeRechargeRecordEntity.getAmount());
            merchantsInfoEntity.setAvailableAmount(newAmount);
            merchantsInfoEntity.setGmtModified(new Date());
            merchantsInfoDao.updateById(merchantsInfoEntity);
        }

        //变更充值记录状态
        financeRechargeRecordEntity.setTxStatus(String.valueOf(status));
        financeRechargeRecordEntity.setGmtModified(new Date());
        financeRechargeRecordDao.updateById(financeRechargeRecordEntity);
        return setResultSuccess();
    }
}