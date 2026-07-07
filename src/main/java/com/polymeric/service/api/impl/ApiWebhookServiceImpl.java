package com.polymeric.service.api.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.channel.PoloConfig;
import com.polymeric.config.channel.PoloMethods;
import com.polymeric.config.channel.UnifiedConfig;
import com.polymeric.constants.Constants;
import com.polymeric.dao.channel.ChannelInfoDao;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsUserCardDao;
import com.polymeric.dao.merchants.MerchantsUserDao;
import com.polymeric.dao.order.OrderBankCardTradeDao;
import com.polymeric.dao.order.OrderIncomeListDao;
import com.polymeric.dao.order.OrderMchCashFlowDao;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;
import com.polymeric.entity.merchants.MerchantsUserEntity;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;
import com.polymeric.entity.order.OrderBankCardTradeEntity;
import com.polymeric.entity.order.OrderIncomeListEntity;
import com.polymeric.entity.order.OrderMchCashFlowEntity;
import com.polymeric.enums.CardStateEnums;
import com.polymeric.enums.KycStateEnums;
import com.polymeric.enums.OrderStatusEnum;
import com.polymeric.enums.OrderTypeEnum;
import com.polymeric.enums.PublicEnums;
import com.polymeric.enums.TradeTypeEnum;
import com.polymeric.enums.TradeTypeEnums;
import com.polymeric.enums.WebhookPoloTypeEnums;
import com.polymeric.query.api.ApiBankCardIdQuery;
import com.polymeric.query.webhook.WebhookQuery;
import com.polymeric.response.polo.CardBalanceRes;
import com.polymeric.response.pub.ApiResponseEntity;
import com.polymeric.service.api.ApiWebhookService;
import com.polymeric.utils.BigDecimalUtils;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.OrderCodeFactory;
import com.polymeric.utils.sign.ApiPoloUtil;

import kotlin.jvm.Synchronized;

@RestController
@Transactional
@CrossOrigin
public class ApiWebhookServiceImpl implements ApiWebhookService {
	
	@Autowired
	private MerchantsUserDao merchantsUserDao;
	
	@Autowired
	private MerchantsUserCardDao merchantsUserCardDao;
	
	@Autowired
	private OrderMchCashFlowDao orderMchCashFlowDao;
	
	@Autowired
	private MerchantsInfoDao merchantsInfoDao;
	
	@Autowired
	private OrderBankCardTradeDao orderBankCardTradeDao;
	
	@Autowired
	private OrderIncomeListDao orderIncomeListDao;
	
	@Autowired
	private ChannelInfoDao channelInfoDao;
	
	
	@Override
	@Synchronized
	public ApiResponseEntity agentNotify(@RequestBody WebhookQuery entity) {
		try {
			if (entity != null && WebhookPoloTypeEnums.KYC_STATUS_CHANGE.getCode().equals(entity.getEventType())) {//KYC状态变更通知
				return this.kycStatusChange(entity);
			}else if(entity != null && WebhookPoloTypeEnums.AUTHORIZATION_3DS.getCode().equals(entity.getEventType())) {//授权3DS通知
				return this.authorization3ds(entity);
			}else if(entity != null && WebhookPoloTypeEnums.CARD_STATUS_CHANGE.getCode().equals(entity.getEventType())) {//银行卡状态变更通知
				return this.cardStatusChange(entity);
			}else if(entity != null && WebhookPoloTypeEnums.CARD_RECHARGE_RESULT.getCode().equals(entity.getEventType())) {//银行卡充值回调
				return this.cardRechargeResult(entity);
			}else if(entity != null && WebhookPoloTypeEnums.TRANSACTION_CREATED.getCode().equals(entity.getEventType())) {//银行卡交易通知
				return this.transactionCreated(entity);
			}else {
				return ApiResponseEntity.error();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	/**
	 * @param entity 
	 * @category polo KYC状态变更通知
	 * @return
	 */
	public ApiResponseEntity kycStatusChange(WebhookQuery entity) {
		MerchantsUserEntity userEntity = merchantsUserDao.findByUid(entity.getUid());
		if(userEntity == null) {
			return ApiResponseEntity.error();
		}
		if(PublicEnums.ONE.getIndex().toString().equals(entity.getAuditState())) {//认证成功
			userEntity.setKycState(KycStateEnums.SUCCESS_APPROVE.getIndex());
		}else if(PublicEnums.TOW.getIndex().toString().equals(entity.getAuditState())) {//认证失败
			userEntity.setKycState(KycStateEnums.ERROR_APPROVE.getIndex());
		}else {
			return ApiResponseEntity.error();
		}
		merchantsUserDao.updateById(userEntity);
		//构建商户回调消息
		MerchantsWebHookMsgEntity msgEntity = ApiMchWebhook.buildMsg(WebhookPoloTypeEnums.KYC_STATUS_CHANGE.getIndex(), userEntity, entity);
		//回调给商户
		ApiMchWebhook.callbackMerchants(msgEntity);
		return ApiResponseEntity.success();
	}
	
	
	/**
	 * @category 授权3DS通知
	 * @param entity
	 * @return
	 */
	public ApiResponseEntity authorization3ds(WebhookQuery entity) {
		MerchantsUserCardEntity cardEntity = merchantsUserCardDao.findUserBankcardId(entity.getUserBankcardId());
		if(cardEntity == null) {
			return ApiResponseEntity.error();
		}
		MerchantsUserEntity userEntity = merchantsUserDao.findByUid(cardEntity.getUserUid());
		if(userEntity == null) {
			return ApiResponseEntity.error();
		}
		// 构建商户回调消息
		MerchantsWebHookMsgEntity msgEntity = ApiMchWebhook.buildMsg(WebhookPoloTypeEnums.AUTHORIZATION_3DS.getIndex(),userEntity, entity);
		// 回调给商户
		ApiMchWebhook.callbackMerchants(msgEntity);
		return ApiResponseEntity.success();
	}
	
	/**
	 * @category 银行卡状态变更
	 * @param entity
	 * @return
	 */
	public ApiResponseEntity cardStatusChange(WebhookQuery entity) {
		MerchantsUserCardEntity cardEntity = merchantsUserCardDao.findUserBankcardId(entity.getUserBankcardId());
		if(cardEntity == null) {
			return ApiResponseEntity.error();
		}
		MerchantsUserEntity userEntity = merchantsUserDao.findByUid(cardEntity.getUserUid());
		if(userEntity == null) {
			return ApiResponseEntity.error();
		}
		MerchantsInfoEntity mchEntity = merchantsInfoDao.selectById(userEntity.getMchId());
		if(mchEntity == null) {
			return ApiResponseEntity.error();
		}
		if("cardFreeze".equals(entity.getStatus())) {//银行卡冻结
			return this.cardFreezeChange(cardEntity,userEntity,mchEntity,entity);
		}else if("cardActive".equals(entity.getStatus())) {//银行卡激活
			return this.cardActiveChange(cardEntity,userEntity,mchEntity,entity);
		}else if("cardClose".equals(entity.getStatus())) {//银行卡关闭
			return this.cardCloseChange(cardEntity,userEntity,mchEntity,entity);
		}else {
			return ApiResponseEntity.error();
		}
	}
	
	/**
	 * @category 银行卡冻结回调
	 * @param entity 
	 * @param userEntity 
	 * @param cardEntity 
	 * @param mchEntity 
	 * @return
	 */
	public ApiResponseEntity cardFreezeChange(MerchantsUserCardEntity cardEntity, MerchantsUserEntity userEntity, MerchantsInfoEntity mchEntity, WebhookQuery entity) {
		cardEntity.setCardState(CardStateEnums.FROZEN.getIndex());
		cardEntity.setCardNum(entity.getCardNo());
		merchantsUserCardDao.updateById(cardEntity);
		// 构建商户回调消息
		MerchantsWebHookMsgEntity msgEntity = ApiMchWebhook.buildMsg(WebhookPoloTypeEnums.CARD_STATUS_CHANGE.getIndex(),userEntity, entity);
		// 回调给商户
		ApiMchWebhook.callbackMerchants(msgEntity);
		return ApiResponseEntity.success();
	}
	
	/**
	 * @category 银行卡激活回调
	 * @param cardEntity
	 * @param userEntity
	 * @param mchEntity 
	 * @param entity
	 * @return
	 */
	public ApiResponseEntity cardActiveChange(MerchantsUserCardEntity cardEntity, MerchantsUserEntity userEntity, MerchantsInfoEntity mchEntity, WebhookQuery entity) {
		//修改银行卡状态及卡号
		cardEntity.setCardState(CardStateEnums.NORMAL.getIndex());
		cardEntity.setCardNum(entity.getCardNo());
		merchantsUserCardDao.updateById(cardEntity);
		//修改商户流水记录状态
		OrderMchCashFlowEntity cashFlowEntity = orderMchCashFlowDao.findOpenCardList(entity.getUserBankcardId(),OrderTypeEnum.OPEN_CARD.getCode(),OrderStatusEnum.PROCESSING.getCode());
		if(cashFlowEntity != null) {
			cashFlowEntity.setOrderState(OrderStatusEnum.SUCCESS.getCode());
			orderMchCashFlowDao.updateById(cashFlowEntity);
			//释放商户冻结金额
			mchEntity.setFreezeAmount(BigDecimalUtils.subtract(mchEntity.getFreezeAmount(), cashFlowEntity.getActualAmount()));
			merchantsInfoDao.updateById(mchEntity);
		}
		//修改银行卡交易记录状态
		OrderBankCardTradeEntity bankCardTradeEntity = orderBankCardTradeDao.findOpenCardList(entity.getUserBankcardId(),TradeTypeEnums.APPLY_CARD.getCode(),OrderStatusEnum.PROCESSING.getCode());
		if(bankCardTradeEntity != null) {
			bankCardTradeEntity.setOrderState(OrderStatusEnum.SUCCESS.getCode());
			orderBankCardTradeDao.updateById(bankCardTradeEntity);
		}
		//修改平台收益记录状态
		OrderIncomeListEntity incomeListEntity = orderIncomeListDao.findOpenCardList(entity.getUserBankcardId(),OrderTypeEnum.OPEN_CARD.getCode(),OrderStatusEnum.PROCESSING.getCode());
		if(incomeListEntity != null) {
			incomeListEntity.setOrderState(OrderStatusEnum.SUCCESS.getCode());
			orderIncomeListDao.updateById(incomeListEntity);
		}
		// 构建商户回调消息
		MerchantsWebHookMsgEntity msgEntity = ApiMchWebhook.buildMsg(WebhookPoloTypeEnums.CARD_STATUS_CHANGE.getIndex(),userEntity, entity);
		// 回调给商户
		ApiMchWebhook.callbackMerchants(msgEntity);
		return ApiResponseEntity.success();
	}
	
	public ApiResponseEntity cardCloseChange(MerchantsUserCardEntity cardEntity, MerchantsUserEntity userEntity, MerchantsInfoEntity mchEntity, WebhookQuery entity) {
		//修改银行卡状态及卡号
		cardEntity.setCardState(CardStateEnums.CANCELED.getIndex());
		cardEntity.setCardNum(entity.getCardNo());
		merchantsUserCardDao.updateById(cardEntity);
		if(entity.getRefundAmount() != null) {
			BigDecimal amount = new BigDecimal(entity.getRefundAmount());
			//处理商户余额，新增交易记录
			ApiMchWebhook.addTradeList(cardEntity,mchEntity,amount);
		}
		// 构建商户回调消息
		MerchantsWebHookMsgEntity msgEntity = ApiMchWebhook.buildMsg(WebhookPoloTypeEnums.CARD_STATUS_CHANGE.getIndex(),userEntity, entity);
		// 回调给商户
		ApiMchWebhook.callbackMerchants(msgEntity);
		return ApiResponseEntity.success();
	}
	
	/**
	 * @category 银行卡充值回调
	 * @param entity
	 * @return
	 */
	public ApiResponseEntity cardRechargeResult(WebhookQuery entity) {
		if (entity.getOrderId() == null) {
			return ApiResponseEntity.error();
		}
		// 商户资金流水
		OrderMchCashFlowEntity mchCashFlowEntity = orderMchCashFlowDao.findOrderNum(entity.getOrderId());
		if(mchCashFlowEntity == null) {
			return ApiResponseEntity.error();
		}
		MerchantsUserEntity userEntity = merchantsUserDao.findByUid(mchCashFlowEntity.getUserUid());
		if(userEntity == null) {
			return ApiResponseEntity.error();
		}
		//查询银行卡余额
		ChannelInfoEntity infoEntity= channelInfoDao.findMchId(mchCashFlowEntity.getMchId());
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity,
				PoloConfig.API_URL, 
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY,
				PoloConfig.AES_KEY,
				PoloMethods.BANKCARD_BALANCE);
		ApiBankCardIdQuery apiBankCardIdQuery = new ApiBankCardIdQuery();
		apiBankCardIdQuery.setUserBankcardId(mchCashFlowEntity.getUserBankcardId());
		ResponseBase base = ApiPoloUtil.postData(mchCashFlowEntity.getUserUid(), null, apiBankCardIdQuery, config);
		if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
			return ApiResponseEntity.error();
		}
		CardBalanceRes cardBalanceRes = JSONObject.parseObject(JSON.toJSONString(base.getData()), CardBalanceRes.class);
		if(cardBalanceRes == null || cardBalanceRes.getBalance() == null) {
			return ApiResponseEntity.error();
		}
		Integer orderState = OrderStatusEnum.PROCESSING.getCode();
		if("SUCCESS".equals(entity.getStatus())) {
			orderState = OrderStatusEnum.SUCCESS.getCode();
		}else if("FAILURE".equals(entity.getStatus())) {
			orderState = OrderStatusEnum.FAILED.getCode();
		}
		//银行卡交易记录
		OrderBankCardTradeEntity bankCardTradeEntity = orderBankCardTradeDao.findOrderNum(entity.getOrderId());
		if(bankCardTradeEntity != null) {
			bankCardTradeEntity.setOrderState(orderState);
			orderBankCardTradeDao.updateById(bankCardTradeEntity);
		}
		//平台收益记录
		OrderIncomeListEntity incomeListEntity = orderIncomeListDao.findOrderNum(entity.getOrderId());
		if(incomeListEntity != null) {
			incomeListEntity.setOrderState(orderState);
			orderIncomeListDao.updateById(incomeListEntity);
		}
		// 商户资金流水状态修改
		mchCashFlowEntity.setOrderState(orderState);
		orderMchCashFlowDao.updateById(mchCashFlowEntity);
		//修改银行卡余额
		MerchantsUserCardEntity cardEntity = merchantsUserCardDao.findUserBankcardId(mchCashFlowEntity.getUserBankcardId());
		if(cardEntity != null) {
			BigDecimal balance = new BigDecimal(cardBalanceRes.getBalance());
			cardEntity.setCardBalance(balance);
			merchantsUserCardDao.updateById(cardEntity);
		}
		//将订单号改为商户的订单号
		entity.setOrderId(mchCashFlowEntity.getMchOrderNum());
		// 构建商户回调消息
		MerchantsWebHookMsgEntity msgEntity = ApiMchWebhook.buildMsg(WebhookPoloTypeEnums.CARD_RECHARGE_RESULT.getIndex(),
				userEntity, entity);
		// 回调给商户
		ApiMchWebhook.callbackMerchants(msgEntity);

		return ApiResponseEntity.success();
		
	}
	
	/**
	 * @category 银行卡交易通知
	 * @param entity
	 * @return
	 */
	public ApiResponseEntity transactionCreated(WebhookQuery entity) {
		try {
			MerchantsUserCardEntity cardEntity = merchantsUserCardDao.findUserBankcardId(entity.getUserBankcardId());
			if(cardEntity == null) {
				return ApiResponseEntity.error();
			}
			MerchantsUserEntity userEntity = merchantsUserDao.findByUid(cardEntity.getUserUid());
			if(userEntity == null) {
				return ApiResponseEntity.error();
			}
			//新增银行卡交易记录
			String orderNum = "WP"+OrderCodeFactory.getOrderCode(null);
			OrderBankCardTradeEntity bankCardTradeEntity = new OrderBankCardTradeEntity();
			bankCardTradeEntity.setOrderNum(orderNum);
			bankCardTradeEntity.setMchOrderNum(orderNum);
			bankCardTradeEntity.setMchAppid(cardEntity.getMchAppid());
			bankCardTradeEntity.setUserUid(cardEntity.getUserUid());
			bankCardTradeEntity.setUserBankcardId(cardEntity.getUserBankcardId());
			bankCardTradeEntity.setTradeType(entity.getTransaction().getTransType());
			bankCardTradeEntity.setOrderRemark(TradeTypeEnum.getName(entity.getTransaction().getTransType())+":"+entity.getTransaction().getMerchantName());
			BigDecimal amount = entity.getTransaction().getLocalCurrencyAmt() == null? BigDecimal.ZERO : new BigDecimal(entity.getTransaction().getLocalCurrencyAmt());
			bankCardTradeEntity.setOrderAmount(amount);
			BigDecimal feeAmount = entity.getTransaction().getFeeAmount() == null ? BigDecimal.ZERO : new BigDecimal(entity.getTransaction().getFeeAmount());
			bankCardTradeEntity.setFeeAmount(feeAmount);
			BigDecimal enterAmount = amount.add(feeAmount);
			bankCardTradeEntity.setEnterAmount(enterAmount);
			bankCardTradeEntity.setOrderCurrency(Constants.USD);
			bankCardTradeEntity.setFeeCurrency(Constants.USD);
			bankCardTradeEntity.setTransCurrency(Constants.USD);
			bankCardTradeEntity.setOrderState(entity.getTransaction().getTransStatus());
			GenericityUtil.setDate(bankCardTradeEntity);
			orderBankCardTradeDao.insert(bankCardTradeEntity);
			//修改银行卡余额
			BigDecimal banlance = entity.getTransaction().getBalance() == null ? BigDecimal.ZERO : new BigDecimal(entity.getTransaction().getBalance());
			cardEntity.setCardBalance(banlance);
			merchantsUserCardDao.updateById(cardEntity);
			// 构建商户回调消息
			MerchantsWebHookMsgEntity msgEntity = ApiMchWebhook.buildMsg(WebhookPoloTypeEnums.TRANSACTION_CREATED.getIndex(),userEntity, entity);
			// 回调给商户
			ApiMchWebhook.callbackMerchants(msgEntity);
			return ApiResponseEntity.success();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}

	
	
}
