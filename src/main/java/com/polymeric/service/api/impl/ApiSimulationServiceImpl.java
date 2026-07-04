package com.polymeric.service.api.impl;

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
import com.polymeric.dao.merchants.MerchantsUserCardDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.query.api.ApiSimulationAuthorizeQuery;
import com.polymeric.query.api.ApiSimulationRefundQuery;
import com.polymeric.query.api.ApiSimulationReversalQuery;
import com.polymeric.query.api.ApiSimulationSettlementQuery;
import com.polymeric.query.api.ApiSimulationTradeQuery;
import com.polymeric.service.api.ApiSimulationService;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.sign.ApiPoloUtil;

@RestController
@Transactional
@CrossOrigin
public class ApiSimulationServiceImpl extends BaseApiService implements ApiSimulationService{
	
	@Autowired
	private MerchantsUserCardDao merchantsUserCardDao;

	@Override
	public ResponseBase simulationAuthorize(HttpServletRequest request, @Valid @RequestBody ApiSimulationAuthorizeQuery authorizeQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, authorizeQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloSimulationAuthorize(infoEntity,authorizeQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * polo 模拟3ds
	 * @param infoEntity
	 * @param authorizeQuery
	 * @return
	 */
	public ResponseBase poloSimulationAuthorize(MerchantsInfoEntity infoEntity, @Valid ApiSimulationAuthorizeQuery authorizeQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.BANKCARD_SIMULATE_3DS);
			
			MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(authorizeQuery.getBankCardId());
			if(userCardEntity == null) {
				return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(), I18nUtil.getMessage("user_bank_card_error"));
			}
			ResponseBase base = ApiPoloUtil.postData(null, null,authorizeQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase simulationTrade(HttpServletRequest request, @Valid @RequestBody ApiSimulationTradeQuery tradeQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, tradeQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloSimulationTrade(infoEntity,tradeQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 模拟交易
	 * @param infoEntity
	 * @param tradeQuery
	 * @return
	 */
	public ResponseBase poloSimulationTrade(MerchantsInfoEntity infoEntity, @Valid ApiSimulationTradeQuery tradeQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.BANKCARD_SIMULATE_AUTH);
			
			MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(tradeQuery.getBankCardId());
			if(userCardEntity == null) {
				return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(), I18nUtil.getMessage("user_bank_card_error"));
			}
			ResponseBase base = ApiPoloUtil.postData(null, null,tradeQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase simulationRefund(HttpServletRequest request, @Valid @RequestBody ApiSimulationRefundQuery refundQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, refundQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloSimulationRefund(infoEntity,refundQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 模拟交易
	 * @param infoEntity
	 * @param refundQuery
	 * @return
	 */
	public ResponseBase poloSimulationRefund(MerchantsInfoEntity infoEntity, @Valid ApiSimulationRefundQuery refundQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.BANKCARD_SIMULATE_AUTH);
			
			ResponseBase base = ApiPoloUtil.postData(null, null,refundQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase simulationSettlement(HttpServletRequest request, @Valid @RequestBody ApiSimulationSettlementQuery settlementQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, settlementQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloSimulationSettlement(infoEntity,settlementQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 模拟结算
	 * @param infoEntity
	 * @param settlementQuery
	 * @return
	 */
	public ResponseBase poloSimulationSettlement(MerchantsInfoEntity infoEntity, @Valid ApiSimulationSettlementQuery settlementQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.BANKCARD_SIMULATE_CLEAR);
			
			ResponseBase base = ApiPoloUtil.postData(null, null,settlementQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase simulationReversal(HttpServletRequest request, @Valid @RequestBody ApiSimulationReversalQuery reversalQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, reversalQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloSimulationReversal(infoEntity,reversalQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	/**
	 * @category polo 模拟退单
	 * @param infoEntity
	 * @param reversalQuery
	 * @return
	 */
	public ResponseBase poloSimulationReversal(MerchantsInfoEntity infoEntity, @Valid ApiSimulationReversalQuery reversalQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.BANKCARD_SIMULATE_REVERSAL);
			
			ResponseBase base = ApiPoloUtil.postData(null, null,reversalQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
