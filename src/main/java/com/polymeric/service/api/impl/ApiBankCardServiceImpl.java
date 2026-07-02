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
import com.polymeric.dao.order.MerchantsOrderDao;
import com.polymeric.dao.order.OrderIncomeListDao;
import com.polymeric.entity.channel.ChannelCardEntity;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;
import com.polymeric.entity.order.MerchantsOrderEntity;
import com.polymeric.entity.order.OrderIncomeListEntity;
import com.polymeric.enums.CardStateEnums;
import com.polymeric.enums.CardTypeEnums;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.enums.OrderStatusEnum;
import com.polymeric.enums.OrderTypeEnum;
import com.polymeric.enums.PublicEnums;
import com.polymeric.query.api.ApiActiveQuery;
import com.polymeric.query.api.ApiCardApplyQuery;
import com.polymeric.query.api.ApiGetCanActiveQuery;
import com.polymeric.query.api.ApiSetPinQuery;
import com.polymeric.response.api.PoloMerchantBankcardRes;
import com.polymeric.response.polo.PoloApplyCardRes;
import com.polymeric.service.api.ApiBankCardService;
import com.polymeric.utils.AesUtils;
import com.polymeric.utils.BigDecimalUtils;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.OrderCodeFactory;
import com.polymeric.utils.sign.ApiPoloUtil;

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
	private MerchantsOrderDao merchantsOrderDao;

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
			//新增商户用户银行卡
			PoloApplyCardRes applyCardRes = JSONObject.parseObject(JSON.toJSONString(base.getData()), PoloApplyCardRes.class);
			this.addMchUserCard(infoEntity, cardEntity, applyCardRes);
			//开卡费用处理
			this.openCardAmount(infoEntity, cardEntity, applyCardRes,OrderStatusEnum.WAIT_PAY.getCode());
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
			this.addMchUserCard(infoEntity, cardEntity, applyCardRes);
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
			ResponseBase base = ApiCheck.checkHeader(request, null);
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
	 * @category polo 设置品
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
	
	/****************************************************************以下是公共方法******************************************************************************/
	/**
	 * @category polo 新增商户用户银行卡
	 * @param infoEntity
	 * @param cardEntity
	 * @param applyCardRes
	 */
	public MerchantsUserCardEntity addMchUserCard(MerchantsInfoEntity infoEntity,MerchantsCardEntity cardEntity,PoloApplyCardRes applyCardRes) {
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
			userCardEntity.setCardState(CardStateEnums.WAITING_ACTIVATE.getIndex());
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
	 * @category polo 开卡交易记录商户余额处理
	 * @param infoEntity
	 * @param cardEntity
	 * @param applyCardRes
	 */
	public void openCardAmount(MerchantsInfoEntity infoEntity,MerchantsCardEntity cardEntity,PoloApplyCardRes applyCardRes,Integer orderState) {
		try {
			//平台收益记录
			OrderIncomeListEntity cardOrderEntity = new OrderIncomeListEntity();
			cardOrderEntity.setOrderNum(OrderCodeFactory.getOrderCode(Long.parseLong(infoEntity.getMerchantsUserData().getApiUid())));
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
			if(OrderStatusEnum.WAIT_PAY.getCode().equals(orderState)) {//处理中
				//商户余额处理
				infoEntity.setAvailableAmount(BigDecimalUtils.subtract(infoEntity.getAvailableAmount(), cardOrderEntity.getSysAmount()));
				infoEntity.setFreezeAmount(BigDecimalUtils.add(infoEntity.getFreezeAmount(), cardOrderEntity.getSysAmount()));
				System.out.println(infoEntity.getFreezeAmount());
				merchantsInfoDao.updateById(infoEntity);
			}else if(OrderStatusEnum.SUCCESS.getCode().equals(orderState)) {
				infoEntity.setAvailableAmount(BigDecimalUtils.subtract(infoEntity.getAvailableAmount(), cardOrderEntity.getSysAmount()));
				merchantsInfoDao.updateById(infoEntity);
			}
			//新增商户资金明细记录
			MerchantsOrderEntity merchantsOrderEntity = new MerchantsOrderEntity();
			String orderNum = "WP"+OrderCodeFactory.getOrderCode(null);
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
			merchantsOrderDao.insert(merchantsOrderEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
