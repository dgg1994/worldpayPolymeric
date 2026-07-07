package com.polymeric.service.api.impl;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsKeyDao;
import com.polymeric.dao.merchants.MerchantsUserCardDao;
import com.polymeric.dao.merchants.MerchantsWebHookMsgDao;
import com.polymeric.dao.order.OrderBankCardTradeDao;
import com.polymeric.dao.order.OrderMchCashFlowDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsKeyEntity;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;
import com.polymeric.entity.merchants.MerchantsUserEntity;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;
import com.polymeric.entity.order.OrderBankCardTradeEntity;
import com.polymeric.entity.order.OrderMchCashFlowEntity;
import com.polymeric.enums.OrderStatusEnum;
import com.polymeric.enums.OrderTypeEnum;
import com.polymeric.enums.TradeTypeEnums;
import com.polymeric.enums.WebHookStateEnum;
import com.polymeric.enums.WebhookPoloTypeEnums;
import com.polymeric.response.pub.ApiResponseEntity;
import com.polymeric.utils.BigDecimalUtils;
import com.polymeric.utils.CallbackHttpSendUtil;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.OrderCodeFactory;
import com.polymeric.utils.sign.RsaSignUtil;

import lombok.Synchronized;

/**
 * @category 回调商户工具类型
 * @author Hlin
 *
 */
@Component
@Transactional
@CrossOrigin
public class ApiMchWebhook {
	
	private static final int[] RETRY_INTERVALS = {60,90,120,180,300,360,420,480,600,600};
	
	private static MerchantsInfoDao merchantsInfoDao;
	
	private static MerchantsWebHookMsgDao merchantsWebHookMsgDao;
	
	private static MerchantsKeyDao merchantsKeyDao;
	
	private static OrderMchCashFlowDao orderMchCashFlowDao;
	
	private static OrderBankCardTradeDao orderBankCardTradeDao;
	
	private static MerchantsUserCardDao merchantsUserCardDao;
	
	@Autowired
	public void setMerchantsInfoDao(MerchantsInfoDao merchantsInfoDao) {
		ApiMchWebhook.merchantsInfoDao = merchantsInfoDao;
	}
	
	@Autowired
	public void setMerchantsWebHookMsgDao(MerchantsWebHookMsgDao merchantsWebHookMsgDao) {
		ApiMchWebhook.merchantsWebHookMsgDao = merchantsWebHookMsgDao;
	}
	
	@Autowired
	public void setMerchantsKeyDao(MerchantsKeyDao merchantsKeyDao) {
		ApiMchWebhook.merchantsKeyDao = merchantsKeyDao;
	}
	
	@Autowired
	public void setOrderMchCashFlowDao(OrderMchCashFlowDao orderMchCashFlowDao) {
		ApiMchWebhook.orderMchCashFlowDao = orderMchCashFlowDao;
	}
	
	@Autowired
	public void setOrderBankCardTradeDao(OrderBankCardTradeDao orderBankCardTradeDao) {
		ApiMchWebhook.orderBankCardTradeDao = orderBankCardTradeDao;
	}
	
	@Autowired
	public void setMerchantsUserCardDao(MerchantsUserCardDao merchantsUserCardDao) {
		ApiMchWebhook.merchantsUserCardDao = merchantsUserCardDao;
	}
	
	
	/**
	 * @category 构建回调消息
	 * @param <T>
	 * @param userEntity
	 * @param requestBody
	 * @return
	 */
	public static <T> MerchantsWebHookMsgEntity buildMsg(Integer msgType,MerchantsUserEntity userEntity,T requestBody) {
		try {
			MerchantsWebHookMsgEntity msgEntity = new MerchantsWebHookMsgEntity();
			msgEntity.setMsgCode(OrderCodeFactory.getMsgCode(null));
			msgEntity.setMchAppid(userEntity.getMchAppid());
			msgEntity.setUid(userEntity.getApiUid());
			msgEntity.setMsgType(msgType);
			msgEntity.setMsgTypeName(WebhookPoloTypeEnums.getName(msgType));
			MerchantsInfoEntity merchantsInfoEntity = merchantsInfoDao.findByAppId(userEntity.getMchAppid());
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
	
	/**
	 * @category 回调商户
	 * @param msgEntity
	 * @return
	 */
	@Synchronized
	public static boolean callbackMerchants(MerchantsWebHookMsgEntity msgEntity) {
		if(msgEntity.getCallbackData() == null || msgEntity.getCallbackData().isEmpty()) {
			return false;
		}
		JSONObject jsonObject = JSONObject.parseObject(msgEntity.getCallbackData());
		//回调参数添加sign
		String sign = null;
		MerchantsKeyEntity keyEntity = merchantsKeyDao.findAppId(msgEntity.getMchAppid());
		if(keyEntity != null && keyEntity.getPrivateKey() != null && !keyEntity.getPrivateKey().isEmpty()) {
			sign = RsaSignUtil.signRequest(null,null,null,msgEntity.getCallbackData(), keyEntity.getPrivateKey());
		}
		//回调商户
		boolean callbackState = false;
		ApiResponseEntity responseEntity = CallbackHttpSendUtil.forwardData(sign,msgEntity.getCallbackUrl(), JSON.toJSONString(jsonObject));
		if(Constants.HTTP_RES_CODE_200.equals(responseEntity.getCode())) {//响应成功
			msgEntity.setStatus(WebHookStateEnum.SUCCESS.getCode());
			callbackState = true;
		}else {
			int retryCount = msgEntity.getRetryCount();
			if (retryCount < RETRY_INTERVALS.length) {
			    int interval = RETRY_INTERVALS[retryCount];
			    Date nextTime = new Date(System.currentTimeMillis() + interval * 1000L);
			    msgEntity.setNextRetryTime(nextTime);
			}
			msgEntity.setStatus(WebHookStateEnum.FAILED.getCode());
		}
		//修改回调消息状态及通知次数
		msgEntity.setRetryCount(msgEntity.getRetryCount() +1);
		msgEntity.setGmtModified(new Date());
		merchantsWebHookMsgDao.updateById(msgEntity);
		return callbackState;
	
	}
	
	/**
	 * @category 处理商户余额退款，新增交易记录
	 * @param cardEntity
	 * @param mchEntity
	 * @param amount
	 */
	public static void addTradeList(MerchantsUserCardEntity cardEntity, MerchantsInfoEntity mchEntity, BigDecimal amount) {
		try {
			String orderNum = "WP"+OrderCodeFactory.getOrderCode(null);
			//交易前商户余额
			BigDecimal beforeAmount = mchEntity.getAvailableAmount();
			//修改商户余额
			mchEntity.setAvailableAmount(BigDecimalUtils.add(mchEntity.getAvailableAmount(),amount));
			merchantsInfoDao.updateById(mchEntity);
			//修改银行卡余额
			cardEntity.setCardBalance(BigDecimal.ZERO);
			merchantsUserCardDao.updateById(cardEntity);
			//新增商户资金明细记录
			OrderMchCashFlowEntity merchantsOrderEntity = new OrderMchCashFlowEntity();
			merchantsOrderEntity.setOrderNum(orderNum);
			merchantsOrderEntity.setMchOrderNum(orderNum);
			merchantsOrderEntity.setUserId(cardEntity.getUserId());
			merchantsOrderEntity.setUserUid(cardEntity.getUserUid());
			merchantsOrderEntity.setUserBankcardId(cardEntity.getUserBankcardId());
			merchantsOrderEntity.setMchId(cardEntity.getMchId());
			merchantsOrderEntity.setMchAppid(cardEntity.getMchAppid());
			merchantsOrderEntity.setTradeType(OrderTypeEnum.CANCEL_CARD.getLable());
			merchantsOrderEntity.setOrderType(OrderTypeEnum.CANCEL_CARD.getCode());
			merchantsOrderEntity.setOrderAmount(amount);
			merchantsOrderEntity.setActualAmount(amount);
			merchantsOrderEntity.setBeforeAmount(beforeAmount);
			merchantsOrderEntity.setAfterAmount(mchEntity.getAvailableAmount());
			merchantsOrderEntity.setOrderState(OrderStatusEnum.SUCCESS.getCode());
			GenericityUtil.setDate(merchantsOrderEntity);
			orderMchCashFlowDao.insert(merchantsOrderEntity);
			//银行卡交易记录
			OrderBankCardTradeEntity bankCardTradeEntity = new OrderBankCardTradeEntity();
			bankCardTradeEntity.setOrderNum(orderNum);
			bankCardTradeEntity.setMchOrderNum(orderNum);
			bankCardTradeEntity.setMchAppid(cardEntity.getMchAppid());
			bankCardTradeEntity.setUserUid(cardEntity.getUserUid());
			bankCardTradeEntity.setUserBankcardId(cardEntity.getUserBankcardId());
			bankCardTradeEntity.setTradeType(TradeTypeEnums.CANCEL_CARD.getCode());
			bankCardTradeEntity.setOrderRemark(TradeTypeEnums.CANCEL_CARD.getEnDesc());
			bankCardTradeEntity.setOrderAmount(amount);
			bankCardTradeEntity.setEnterAmount(amount);
			bankCardTradeEntity.setFeeAmount(BigDecimal.ZERO);
			bankCardTradeEntity.setOrderCurrency(Constants.USD);
			bankCardTradeEntity.setFeeCurrency(Constants.USD);
			bankCardTradeEntity.setTransCurrency(Constants.USD);
			bankCardTradeEntity.setOrderState(OrderStatusEnum.SUCCESS.getCode());
			GenericityUtil.setDate(bankCardTradeEntity);
			orderBankCardTradeDao.insert(bankCardTradeEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	

}
