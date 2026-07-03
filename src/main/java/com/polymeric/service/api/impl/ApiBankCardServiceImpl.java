package com.polymeric.service.api.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.channel.PoloConfig;
import com.polymeric.config.channel.PoloMethods;
import com.polymeric.config.channel.UnifiedConfig;
import com.polymeric.constants.Constants;
import com.polymeric.dao.channel.ChannelCardDao;
import com.polymeric.dao.merchants.MerchantsCardDao;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsUserCardDao;
import com.polymeric.dao.order.OrderBankCardTradeDao;
import com.polymeric.dao.order.OrderIncomeListDao;
import com.polymeric.dao.order.OrderMchCashFlowDao;
import com.polymeric.entity.channel.ChannelCardEntity;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;
import com.polymeric.entity.order.OrderBankCardTradeEntity;
import com.polymeric.entity.order.OrderIncomeListEntity;
import com.polymeric.entity.order.OrderMchCashFlowEntity;
import com.polymeric.enums.CardStateEnums;
import com.polymeric.enums.CardTypeEnums;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.enums.OrderStatusEnum;
import com.polymeric.enums.OrderTypeEnum;
import com.polymeric.enums.PublicEnums;
import com.polymeric.enums.TradeTypeEnums;
import com.polymeric.query.api.ApiActiveQuery;
import com.polymeric.query.api.ApiBankCardIdQuery;
import com.polymeric.query.api.ApiCardApplyQuery;
import com.polymeric.query.api.ApiCardRechargeQuery;
import com.polymeric.query.api.ApiGetCanActiveQuery;
import com.polymeric.query.api.ApiSetPinQuery;
import com.polymeric.query.api.ApiUpdateCardStatusQuery;
import com.polymeric.query.api.ApiUpdateEmailQuery;
import com.polymeric.response.api.PoloMerchantBankcardRes;
import com.polymeric.response.polo.CardBalanceRes;
import com.polymeric.response.polo.CardInfoRes;
import com.polymeric.response.polo.PoloApplyCardRes;
import com.polymeric.service.api.ApiBankCardService;
import com.polymeric.utils.AesUtils;
import com.polymeric.utils.BigDecimalUtils;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.OrderCodeFactory;
import com.polymeric.utils.sign.ApiPoloUtil;

import lombok.Synchronized;

@RestController
@Transactional
@CrossOrigin
public class ApiBankCardServiceImpl extends BaseApiService implements ApiBankCardService{
	
	@Autowired
	private MerchantsCardDao merchantsCardDao;
	
	@Autowired
	private MerchantsUserCardDao merchantsUserCardDao;
	
	@Autowired
	private ChannelCardDao channelCardDao;
	
	@Autowired
	private OrderIncomeListDao orderIncomeListDao;
	
	@Autowired
	private MerchantsInfoDao merchantsInfoDao;
	
	@Autowired
	private OrderMchCashFlowDao orderMchCashFlowDao;
	
	@Autowired
	private OrderBankCardTradeDao orderBankCardTradeDao;

	@Override
	public ResponseBase merchantBankCardList(HttpServletRequest request) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, null);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloMerchantBankcardList(infoEntity);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category polo查询商户可用卡产品列表信息
	 * @param infoEntity
	 * @return
	 */
	public ResponseBase poloMerchantBankcardList(MerchantsInfoEntity infoEntity) {
		List<PoloMerchantBankcardRes> cardList = merchantsCardDao.findMchId(infoEntity.getId(),PublicEnums.ONE.getIndex());
		return setResultSuccess(cardList);
	}

	@Override
	public ResponseBase userCardList(HttpServletRequest request) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, null);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloUserCardList(infoEntity);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 用户银行卡列表
	 * @param infoEntity
	 * @return
	 */
	public ResponseBase poloUserCardList(MerchantsInfoEntity infoEntity) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(),
				PoloConfig.API_URL, 
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY,
				PoloConfig.AES_KEY,
				PoloMethods.USER_CARD_LIST);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 调用三方接口
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, null, config);
		return base;
	}

	@Override
	@Synchronized
	public ResponseBase applyCard(HttpServletRequest request, @Valid @RequestBody ApiCardApplyQuery cardApplyQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, cardApplyQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloApplyCard(infoEntity,cardApplyQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 银行卡申请
	 * @param infoEntity
	 * @param cardApplyQuery
	 * @return
	 */
	public ResponseBase poloApplyCard(MerchantsInfoEntity infoEntity, @Valid ApiCardApplyQuery cardApplyQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(),
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY,
					PoloConfig.AES_KEY,
					PoloMethods.BANKCARD_APPLY);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			//校验产品信息
			ResponseBase cardBase = ApiCheck.checkCard(cardApplyQuery.getProductId(), infoEntity);
			if(!Constants.HTTP_RES_CODE_200.equals(cardBase.getCode())) {
				return cardBase;
			}
			//商户产品信息
			MerchantsCardEntity cardEntity = JSONObject.parseObject(JSON.toJSONString(cardBase.getData()), MerchantsCardEntity.class);
			//判断商户余额是否充足
			if (BigDecimalUtils.isLessThan(infoEntity.getAvailableAmount(), cardEntity.getApplyFee())) {
				return setResultError(ErrorCodeEnum.AMOUNT_SCARCITY.getCode(), I18nUtil.getMessage("amount_scarcity"));
			}
			// 调用三方接口
			cardApplyQuery.setProductId(cardEntity.getCardId());
			ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, cardApplyQuery, config);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			if(cardApplyQuery.getDeliveryAddressId() == null) {
				//新增商户用户银行卡
				PoloApplyCardRes applyCardRes = JSONObject.parseObject(JSON.toJSONString(base.getData()), PoloApplyCardRes.class);
				this.addMchUserCard(infoEntity, cardEntity, applyCardRes,CardStateEnums.WAITING_ACTIVATE.getIndex());
				//开卡费用处理
				this.openCardAmount(infoEntity, cardEntity, applyCardRes,OrderStatusEnum.PROCESSING.getCode());
			}
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase getCanActive(HttpServletRequest request, @Valid @RequestBody ApiGetCanActiveQuery activeQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, activeQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloGetCanActive(infoEntity,activeQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category polo 银行卡是否可激活
	 * @param infoEntity
	 * @param activeQuery
	 * @return
	 */
	public ResponseBase poloGetCanActive(MerchantsInfoEntity infoEntity, @Valid ApiGetCanActiveQuery activeQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(),
				PoloConfig.API_URL, 
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.BANKCARD_CANACTIVE);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, activeQuery, config);
		return base;
	}

	@Override
	@Synchronized
	public ResponseBase active(HttpServletRequest request, @Valid @RequestBody ApiActiveQuery activeQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, activeQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloActive(infoEntity,activeQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 银行卡激活（实体卡用户）
	 * @param infoEntity
	 * @param activeQuery
	 * @return
	 */
	public ResponseBase poloActive(MerchantsInfoEntity infoEntity, @Valid ApiActiveQuery activeQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL,
					PoloConfig.APP_ID, 
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.BANKCARD_ACTIVE);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			//校验产品信息
			ResponseBase cardBase = ApiCheck.checkCard(activeQuery.getProductId(), infoEntity);
			if(!Constants.HTTP_RES_CODE_200.equals(cardBase.getCode())) {
				return cardBase;
			}
			//产品信息
			MerchantsCardEntity cardEntity = JSONObject.parseObject(JSON.toJSONString(cardBase.getData()), MerchantsCardEntity.class);
			//判断商户余额是否充足
			if (BigDecimalUtils.isLessThan(infoEntity.getAvailableAmount(), cardEntity.getApplyFee())) {
				return setResultError(ErrorCodeEnum.AMOUNT_SCARCITY.getCode(), I18nUtil.getMessage("amount_scarcity"));
			}
			// 调用三方接口
			activeQuery.setProductId(cardEntity.getCardId());
			ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null,activeQuery, config);
			if (!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			//新增商户用户银行卡
			PoloApplyCardRes applyCardRes = JSONObject.parseObject(JSON.toJSONString(base.getData()), PoloApplyCardRes.class);
			this.addMchUserCard(infoEntity, cardEntity, applyCardRes,CardStateEnums.ACTIVATING.getIndex());
			//开卡费用处理
			this.openCardAmount(infoEntity, cardEntity, applyCardRes,OrderStatusEnum.SUCCESS.getCode());
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	

	@Override
	public ResponseBase setPin(HttpServletRequest request, @Valid @RequestBody ApiSetPinQuery setPinQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, setPinQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloSetPin(infoEntity,setPinQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 设置 pin
	 * @param infoEntity
	 * @param setPinQuery 
	 * @return
	 */
	public ResponseBase poloSetPin(MerchantsInfoEntity infoEntity, @Valid ApiSetPinQuery setPinQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.BANKCARD_ACTIVE);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(setPinQuery.getUserBankcardId());
			if(userCardEntity == null) {
				return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(), I18nUtil.getMessage("user_bank_card_error"));
			}
			if(!CardTypeEnums.PHYSICAL.getCode().equals(userCardEntity.getCardType())) {
				return setResultError(ErrorCodeEnum.USER_BANK_CARD_TYPE_ERROR.getCode(), I18nUtil.getMessage("user_bank_card_type_error"));
			}
			String aesPin = AesUtils.encrypt(setPinQuery.getPin(), config.getAesKey());
			setPinQuery.setPin(aesPin);
			ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null,setPinQuery, config);
			if (!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			userCardEntity.setPinNum(aesPin);
			merchantsUserCardDao.updateById(userCardEntity);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	@Override
	public ResponseBase getBalance(HttpServletRequest request, @Valid @RequestBody ApiBankCardIdQuery bankCardIdQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, bankCardIdQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloGetBalance(infoEntity,bankCardIdQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 查询银行卡余额
	 * @param infoEntity
	 * @param bankCardIdQuery
	 * @return
	 */
	public ResponseBase poloGetBalance(MerchantsInfoEntity infoEntity, @Valid ApiBankCardIdQuery bankCardIdQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(), 
				PoloConfig.API_URL,
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.BANKCARD_BALANCE);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null,bankCardIdQuery, config);
		if (!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
			return base;
		}
		CardBalanceRes cardBalanceRes = JSONObject.parseObject(JSON.toJSONString(base.getData()), CardBalanceRes.class);
		if(cardBalanceRes != null && cardBalanceRes.getBalance() != null) {
			//更新银行卡余额
			MerchantsUserCardEntity cardEntity = merchantsUserCardDao.findUserBankcardId(bankCardIdQuery.getUserBankcardId());
			if(cardEntity != null) {
				BigDecimal decimal = new BigDecimal(cardBalanceRes.getBalance());
				cardEntity.setCardBalance(decimal);
				merchantsUserCardDao.updateById(cardEntity);
			}
		}
		return base;
	}
	
	
	@Override
	@Synchronized
	public ResponseBase recharge(HttpServletRequest request, @Valid @RequestBody ApiCardRechargeQuery cardRechargeQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, cardRechargeQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloRecharge(infoEntity,cardRechargeQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo银行卡充值
	 * @param infoEntity
	 * @param cardRechargeQuery
	 * @return
	 */
	public ResponseBase poloRecharge(MerchantsInfoEntity infoEntity, @Valid ApiCardRechargeQuery cardRechargeQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL,
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.BANKCARD_RECHARGE);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			//判断银行卡是否存在
			MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(cardRechargeQuery.getUserBankcardId());
			if(userCardEntity == null) {
				return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(), I18nUtil.getMessage("user_bank_card_error"));
			}
			//判断商户余额是否充值
			if (BigDecimalUtils.isLessThan(infoEntity.getAvailableAmount(), cardRechargeQuery.getAmount())) {
				return setResultError(ErrorCodeEnum.AMOUNT_SCARCITY.getCode(), I18nUtil.getMessage("amount_scarcity"));
			}
			//更换为平台单号
			String mchOrderNum = cardRechargeQuery.getRequestOrderId();
			String orderNum = "WP"+OrderCodeFactory.getOrderCode(null);
			cardRechargeQuery.setRequestOrderId(orderNum);
			//调用三方
			ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null,cardRechargeQuery, config);
			if (!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			//各记录及商户余额处理
			this.rechargeCardAmount(infoEntity,userCardEntity,cardRechargeQuery,mchOrderNum);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	@Override
	public ResponseBase updateStatus(HttpServletRequest request, @Valid @RequestBody ApiUpdateCardStatusQuery updateCardStatusQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, updateCardStatusQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloUpdateStatus(infoEntity,updateCardStatusQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 更新银行卡状态
	 * @param infoEntity
	 * @param updateCardStatusQuery
	 * @return
	 */
	public ResponseBase poloUpdateStatus(MerchantsInfoEntity infoEntity, @Valid ApiUpdateCardStatusQuery updateCardStatusQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(),
				PoloConfig.API_URL,
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.BANKCARD_UPDATE_STATUS);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 判断银行卡是否存在
		MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(updateCardStatusQuery.getUserBankcardId());
		if (userCardEntity == null) {
			return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(),I18nUtil.getMessage("user_bank_card_error"));
		}
		
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null,updateCardStatusQuery, config);
//		if (!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
//			return base;
//		}
//		if(updateCardStatusQuery.getEnable()) {//解冻
//			userCardEntity.setCardState(CardStateEnums.NORMAL.getIndex());
//		}else {//冻结
//			userCardEntity.setCardState(CardStateEnums.FROZEN.getIndex());
//		}
//		merchantsUserCardDao.updateById(userCardEntity);
		return base;
		
	}
	
	
	@Override
	public ResponseBase close(HttpServletRequest request, @Valid @RequestBody ApiBankCardIdQuery bankCardIdQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, bankCardIdQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloClose(infoEntity,bankCardIdQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category polo银行卡注销
	 * @param infoEntity
	 * @param bankCardIdQuery
	 * @return
	 */
	public ResponseBase poloClose(MerchantsInfoEntity infoEntity, @Valid ApiBankCardIdQuery bankCardIdQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(),
				PoloConfig.API_URL,
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY,
				PoloConfig.AES_KEY,
				PoloMethods.BANKCARD_CLOSE);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 判断银行卡是否存在
		MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(bankCardIdQuery.getUserBankcardId());
		if (userCardEntity == null) {
			return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(),I18nUtil.getMessage("user_bank_card_error"));
		}
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null,bankCardIdQuery, config);
		return base;
	}
	
	@Override
	public ResponseBase info(HttpServletRequest request, @Valid @RequestBody ApiBankCardIdQuery bankCardIdQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, bankCardIdQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloInfo(infoEntity,bankCardIdQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category polo 查询银行卡信息
	 * @param infoEntity
	 * @param bankCardIdQuery
	 * @return
	 */
	public ResponseBase poloInfo(MerchantsInfoEntity infoEntity, @Valid ApiBankCardIdQuery bankCardIdQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(), 
				PoloConfig.API_URL,
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.BANKCARD_INFO);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 判断银行卡是否存在
		MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(bankCardIdQuery.getUserBankcardId());
		if (userCardEntity == null) {
			return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(),I18nUtil.getMessage("user_bank_card_error"));
		}
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, bankCardIdQuery,config);
		if (!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
			return base;
		}
		CardInfoRes cardInfoRes = JSONObject.parseObject(JSON.toJSONString(base.getData()), CardInfoRes.class);
		if(cardInfoRes != null && cardInfoRes.getStatus() != null) {
			userCardEntity.setCardState(cardInfoRes.getStatus());		
			merchantsUserCardDao.updateById(userCardEntity);
			this.poloGetBalance(infoEntity, bankCardIdQuery);
		}
		return base;
	}
	
	@Override
	public ResponseBase updateEmail(HttpServletRequest request, @Valid @RequestBody ApiUpdateEmailQuery updateEmailQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, updateEmailQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloUpdateEmail(infoEntity,updateEmailQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category polo 更新银行卡邮箱
	 * @param infoEntity
	 * @param updateEmailQuery
	 * @return
	 */
	public ResponseBase poloUpdateEmail(MerchantsInfoEntity infoEntity, @Valid ApiUpdateEmailQuery updateEmailQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(), 
				PoloConfig.API_URL, 
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.BANKCARD_UPDATE_EMAIL);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 判断银行卡是否存在
		MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(updateEmailQuery.getUserBankcardId());
		if (userCardEntity == null) {
			return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(),I18nUtil.getMessage("user_bank_card_error"));
		}
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, updateEmailQuery,config);
		return base;
	}
	
	@Override
	public ResponseBase queryPin(HttpServletRequest request, @Valid @RequestBody ApiBankCardIdQuery bankCardIdQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, bankCardIdQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloQueryPin(infoEntity,bankCardIdQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	/**
	 * @category polo 查询 pin
	 * @param infoEntity
	 * @param bankCardIdQuery
	 * @return
	 */
	public ResponseBase poloQueryPin(MerchantsInfoEntity infoEntity, @Valid ApiBankCardIdQuery bankCardIdQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(),
				PoloConfig.API_URL, 
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.BANKCARD_QUERYPIN);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 判断银行卡是否存在
		MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(bankCardIdQuery.getUserBankcardId());
		if (userCardEntity == null) {
			return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(),I18nUtil.getMessage("user_bank_card_error"));
		}
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, bankCardIdQuery,config);
		return base;
	}
	
	
	
	
	
	/****************************************************************以下是公共方法******************************************************************************/
	/**
	 * @category polo 新增商户用户银行卡
	 * @param infoEntity
	 * @param cardEntity
	 * @param applyCardRes
	 * @param cardState 
	 */
	public MerchantsUserCardEntity addMchUserCard(MerchantsInfoEntity infoEntity,MerchantsCardEntity cardEntity,PoloApplyCardRes applyCardRes, Integer cardState) {
		try {
			MerchantsUserCardEntity userCardEntity = new MerchantsUserCardEntity();
			userCardEntity.setUserId(infoEntity.getMerchantsUserData().getId());
			userCardEntity.setUserUid(infoEntity.getMerchantsUserData().getApiUid());
			userCardEntity.setMchId(infoEntity.getId());
			userCardEntity.setMchAppid(infoEntity.getAppId());
			userCardEntity.setCardId(cardEntity.getId());
			userCardEntity.setCardApiId(cardEntity.getCardId());
			userCardEntity.setUserBankcardId(applyCardRes.getUserBankcardId());
			userCardEntity.setCardType(cardEntity.getBankCardNature());
			userCardEntity.setCardNum(applyCardRes.getCardNo());
			userCardEntity.setCardBalance(BigDecimal.ZERO);
			userCardEntity.setCardState(cardState);
			userCardEntity.setOrderNum(applyCardRes.getOrderNo());
			GenericityUtil.setDate(userCardEntity);
			merchantsUserCardDao.insert(userCardEntity);
			return userCardEntity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 开卡平台收益商户交易记录商户余额处理
	 * @param infoEntity
	 * @param cardEntity
	 * @param applyCardRes
	 */
	public void openCardAmount(MerchantsInfoEntity infoEntity,MerchantsCardEntity cardEntity,PoloApplyCardRes applyCardRes,Integer orderState) {
		try {
			//平台收益记录
			String orderNum = "WP"+OrderCodeFactory.getOrderCode(null);
			OrderIncomeListEntity cardOrderEntity = new OrderIncomeListEntity();
			cardOrderEntity.setOrderNum(orderNum);
			cardOrderEntity.setChannelId(infoEntity.getChannelId());
			cardOrderEntity.setChannelCode(infoEntity.getChannelCode());
			cardOrderEntity.setMchId(infoEntity.getId());
			cardOrderEntity.setMchAppid(infoEntity.getAppId());
			cardOrderEntity.setUserId(infoEntity.getMerchantsUserData().getId());
			cardOrderEntity.setUserUid(infoEntity.getMerchantsUserData().getApiUid());
			cardOrderEntity.setCardId(cardEntity.getId());
			cardOrderEntity.setCardApiId(cardEntity.getCardId());
			cardOrderEntity.setUserBankcardId(applyCardRes.getUserBankcardId());
			cardOrderEntity.setCardType(cardEntity.getBankCardNature());
			cardOrderEntity.setOrderAmount(cardEntity.getApplyFee());
			cardOrderEntity.setOrderType(OrderTypeEnum.OPEN_CARD.getCode());
			ChannelCardEntity channelCardEntity = channelCardDao.selectById(cardEntity.getChannelCardId());
			if(channelCardEntity != null) {
				cardOrderEntity.setChannelAmount(channelCardEntity.getApplyFee());		
			}
			cardOrderEntity.setChannelRates(BigDecimal.ZERO);//开卡没有费率
			cardOrderEntity.setSysRates(BigDecimal.ZERO);//开卡没有费率
			cardOrderEntity.setSysAmount(cardEntity.getApplyFee());
			cardOrderEntity.setProfitAmount(BigDecimalUtils.subtract(cardOrderEntity.getSysAmount(), cardOrderEntity.getChannelAmount()));
			cardOrderEntity.setOrderState(orderState);
			GenericityUtil.setDate(cardOrderEntity);
			orderIncomeListDao.insert(cardOrderEntity);
			//交易前商户余额
			BigDecimal beforeAmount = infoEntity.getAvailableAmount();
			//商户余额修改
			if(OrderStatusEnum.PROCESSING.getCode().equals(orderState)) {//处理中
				//商户余额处理
				infoEntity.setAvailableAmount(BigDecimalUtils.subtract(infoEntity.getAvailableAmount(), cardOrderEntity.getSysAmount()));
				infoEntity.setFreezeAmount(BigDecimalUtils.add(infoEntity.getFreezeAmount(), cardOrderEntity.getSysAmount()));
				merchantsInfoDao.updateById(infoEntity);
			}else if(OrderStatusEnum.SUCCESS.getCode().equals(orderState)) {
				infoEntity.setAvailableAmount(BigDecimalUtils.subtract(infoEntity.getAvailableAmount(), cardOrderEntity.getSysAmount()));
				merchantsInfoDao.updateById(infoEntity);
			}
			//新增商户资金明细记录
			OrderMchCashFlowEntity merchantsOrderEntity = new OrderMchCashFlowEntity();
			merchantsOrderEntity.setOrderNum(orderNum);
			merchantsOrderEntity.setMchOrderNum(orderNum);
			merchantsOrderEntity.setUserId(infoEntity.getMerchantsUserData().getId());
			merchantsOrderEntity.setUserUid(infoEntity.getMerchantsUserData().getApiUid());
			merchantsOrderEntity.setUserBankcardId(applyCardRes.getUserBankcardId());
			merchantsOrderEntity.setMchId(infoEntity.getId());
			merchantsOrderEntity.setMchAppid(infoEntity.getAppId());
			merchantsOrderEntity.setTradeType(OrderTypeEnum.OPEN_CARD.getLable());
			merchantsOrderEntity.setOrderType(OrderTypeEnum.OPEN_CARD.getCode());
			merchantsOrderEntity.setOrderAmount(cardEntity.getApplyFee());
			merchantsOrderEntity.setActualAmount(cardEntity.getApplyFee());
			merchantsOrderEntity.setBeforeAmount(beforeAmount);
			merchantsOrderEntity.setAfterAmount(infoEntity.getAvailableAmount());
			merchantsOrderEntity.setOrderState(orderState);
			GenericityUtil.setDate(merchantsOrderEntity);
			orderMchCashFlowDao.insert(merchantsOrderEntity);
			//银行卡交易记录
			OrderBankCardTradeEntity bankCardTradeEntity = new OrderBankCardTradeEntity();
			bankCardTradeEntity.setOrderNum(orderNum);
			bankCardTradeEntity.setMchOrderNum(orderNum);
			bankCardTradeEntity.setMchAppid(merchantsOrderEntity.getMchAppid());
			bankCardTradeEntity.setUserUid(merchantsOrderEntity.getUserUid());
			bankCardTradeEntity.setUserBankcardId(merchantsOrderEntity.getUserBankcardId());
			bankCardTradeEntity.setTradeType(TradeTypeEnums.APPLY_CARD.getCode());
			bankCardTradeEntity.setOrderAmount(merchantsOrderEntity.getOrderAmount());
			bankCardTradeEntity.setEnterAmount(merchantsOrderEntity.getOrderAmount());
			bankCardTradeEntity.setFeeAmount(BigDecimal.ZERO);
			bankCardTradeEntity.setOrderCurrency(Constants.USD);
			bankCardTradeEntity.setFeeCurrency(Constants.USD);
			bankCardTradeEntity.setTransCurrency(Constants.USD);
			bankCardTradeEntity.setOrderState(orderState);
			bankCardTradeEntity.setOrderRemark(TradeTypeEnums.APPLY_CARD.getEnDesc());
			GenericityUtil.setDate(bankCardTradeEntity);
			orderBankCardTradeDao.insert(bankCardTradeEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category polo 卡充值各记录及商户余额处理
	 * @param infoEntity
	 * @param userCardEntity
	 * @param cardRechargeQuery
	 * @param orderNum
	 * @param mchOrderNum 
	 */
	public void rechargeCardAmount(MerchantsInfoEntity infoEntity, MerchantsUserCardEntity userCardEntity, ApiCardRechargeQuery cardRechargeQuery, String mchOrderNum) {
		try {
			//平台收益记录
			OrderIncomeListEntity cardOrderEntity = new OrderIncomeListEntity();
			cardOrderEntity.setOrderNum(cardRechargeQuery.getRequestOrderId());
			cardOrderEntity.setChannelId(null);
			cardOrderEntity.setChannelId(infoEntity.getChannelId());
			cardOrderEntity.setChannelCode(infoEntity.getChannelCode());
			cardOrderEntity.setMchId(infoEntity.getId());
			cardOrderEntity.setMchAppid(infoEntity.getAppId());
			cardOrderEntity.setUserId(infoEntity.getMerchantsUserData().getId());
			cardOrderEntity.setUserUid(infoEntity.getMerchantsUserData().getApiUid());
			cardOrderEntity.setCardId(userCardEntity.getCardId());
			cardOrderEntity.setCardApiId(userCardEntity.getCardApiId());
			cardOrderEntity.setUserBankcardId(userCardEntity.getUserBankcardId());
			cardOrderEntity.setCardType(userCardEntity.getCardType());
			cardOrderEntity.setOrderType(OrderTypeEnum.CARD_TOP_UP.getCode());
			cardOrderEntity.setOrderAmount(cardRechargeQuery.getAmount());
			//查商户银行卡
			MerchantsCardEntity merchantsCardEntity = merchantsCardDao.selectById(userCardEntity.getCardId());
			if(merchantsCardEntity != null) {
				cardOrderEntity.setSysRates(merchantsCardEntity.getRechargeFee() == null ? BigDecimal.ZERO :merchantsCardEntity.getRechargeFee());
			}
			cardOrderEntity.setSysAmount(BigDecimalUtils.calculateWithRate(cardOrderEntity.getOrderAmount(), cardOrderEntity.getSysRates()));
			ChannelCardEntity channelCardEntity = channelCardDao.findMchCardId(userCardEntity.getCardId());
			if(channelCardEntity != null) {
				cardOrderEntity.setChannelRates(channelCardEntity.getRechargeFee() == null ? BigDecimal.ZERO : channelCardEntity.getRechargeFee());
			}
			cardOrderEntity.setChannelAmount(BigDecimalUtils.calculateWithRate(cardOrderEntity.getOrderAmount(), cardOrderEntity.getChannelRates()));
			cardOrderEntity.setProfitAmount(BigDecimalUtils.subtract(cardOrderEntity.getSysAmount(), cardOrderEntity.getChannelAmount()));
			cardOrderEntity.setOrderState(OrderStatusEnum.PROCESSING.getCode());
			GenericityUtil.setDate(cardOrderEntity);
			orderIncomeListDao.insert(cardOrderEntity);
			//商户余额扣除
			BigDecimal beforeAmount = infoEntity.getAvailableAmount();
			infoEntity.setAvailableAmount(BigDecimalUtils.subtract(infoEntity.getAvailableAmount(), cardOrderEntity.getSysAmount()));
			infoEntity.setFreezeAmount(BigDecimalUtils.add(infoEntity.getFreezeAmount(), cardOrderEntity.getSysAmount()));
			merchantsInfoDao.updateById(infoEntity);
			//新增商户资金明细记录
			OrderMchCashFlowEntity merchantsOrderEntity = new OrderMchCashFlowEntity();
			merchantsOrderEntity.setOrderNum(cardRechargeQuery.getRequestOrderId());
			merchantsOrderEntity.setMchOrderNum(mchOrderNum);
			merchantsOrderEntity.setMchId(infoEntity.getId());
			merchantsOrderEntity.setMchAppid(infoEntity.getAppId());
			merchantsOrderEntity.setUserId(infoEntity.getMerchantsUserData().getId());
			merchantsOrderEntity.setUserUid(infoEntity.getMerchantsUserData().getApiUid());
			merchantsOrderEntity.setUserBankcardId(userCardEntity.getUserBankcardId());
			merchantsOrderEntity.setTradeType(OrderTypeEnum.CARD_TOP_UP.getLable());
			merchantsOrderEntity.setOrderType(OrderTypeEnum.CARD_TOP_UP.getCode());
			merchantsOrderEntity.setOrderAmount(cardRechargeQuery.getAmount());
			merchantsOrderEntity.setActualAmount(cardOrderEntity.getSysAmount());
			merchantsOrderEntity.setBeforeAmount(beforeAmount);
			merchantsOrderEntity.setAfterAmount(infoEntity.getAvailableAmount());
			merchantsOrderEntity.setOrderState(OrderStatusEnum.PROCESSING.getCode());
			GenericityUtil.setDate(merchantsOrderEntity);
			orderMchCashFlowDao.insert(merchantsOrderEntity);
			
			//银行卡交易记录
			OrderBankCardTradeEntity bankCardTradeEntity = new OrderBankCardTradeEntity();
			bankCardTradeEntity.setOrderNum(cardRechargeQuery.getRequestOrderId());
			bankCardTradeEntity.setMchOrderNum(mchOrderNum);
			bankCardTradeEntity.setMchAppid(merchantsOrderEntity.getMchAppid());
			bankCardTradeEntity.setUserUid(merchantsOrderEntity.getUserUid());
			bankCardTradeEntity.setUserBankcardId(merchantsOrderEntity.getUserBankcardId());
			bankCardTradeEntity.setTradeType(TradeTypeEnums.RECHARGE.getCode());
			bankCardTradeEntity.setOrderAmount(merchantsOrderEntity.getOrderAmount());
			bankCardTradeEntity.setEnterAmount(merchantsOrderEntity.getActualAmount());
			bankCardTradeEntity.setFeeAmount(BigDecimalUtils.subtract(merchantsOrderEntity.getActualAmount(),merchantsOrderEntity.getOrderAmount()));
			bankCardTradeEntity.setOrderCurrency(Constants.USD);
			bankCardTradeEntity.setFeeCurrency(Constants.USD);
			bankCardTradeEntity.setTransCurrency(Constants.USD);
			bankCardTradeEntity.setOrderState(OrderStatusEnum.PROCESSING.getCode());
			bankCardTradeEntity.setOrderRemark(TradeTypeEnums.RECHARGE.getEnDesc());
			GenericityUtil.setDate(bankCardTradeEntity);
			orderBankCardTradeDao.insert(bankCardTradeEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		
		
	}









}
