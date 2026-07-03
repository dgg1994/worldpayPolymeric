package com.polymeric.service.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.query.api.ApiAddressAddQuery;
import com.polymeric.query.api.ApiAddressUpdateQuery;
import com.polymeric.query.api.ApiDeliveryInfoQuery;
import com.polymeric.query.api.ApiDeliveryRegionQuery;
import com.polymeric.service.api.ApiDeliveryService;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.sign.ApiPoloUtil;

@RestController
@Transactional
@CrossOrigin
public class ApiDeliveryServiceImpl extends BaseApiService implements ApiDeliveryService{

	@Override
	public ResponseBase deliveryRegion(HttpServletRequest request, @Valid @RequestBody ApiDeliveryRegionQuery regionQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, regionQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloDeliveryRegion(infoEntity,regionQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public ResponseBase poloDeliveryRegion(MerchantsInfoEntity infoEntity, @Valid ApiDeliveryRegionQuery regionQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(),
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY,
					PoloConfig.AES_KEY,
					PoloMethods.DELIVERY_REGION);
			
			ResponseBase base = ApiPoloUtil.postData(null, null, regionQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase addressAdd(HttpServletRequest request, @Valid @RequestBody ApiAddressAddQuery addressAddQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, addressAddQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloAddressAdd(infoEntity,addressAddQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 添加邮寄地址
	 * @param infoEntity
	 * @param addressAddQuery
	 * @return
	 */
	public ResponseBase poloAddressAdd(MerchantsInfoEntity infoEntity, @Valid ApiAddressAddQuery addressAddQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(),
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY,
					PoloConfig.AES_KEY,
					PoloMethods.DELIVERY_ADDRESS_ADD);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			// 调用三方接口
			ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, addressAddQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase addressUpdate(HttpServletRequest request,  @Valid @RequestBody ApiAddressUpdateQuery addressUpdateQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, addressUpdateQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloAddressUpdate(infoEntity,addressUpdateQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 邮寄地址更新
	 * @param infoEntity
	 * @param addressUpdateQuery
	 * @return
	 */
	public ResponseBase poloAddressUpdate(MerchantsInfoEntity infoEntity, @Valid ApiAddressUpdateQuery addressUpdateQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(),
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY,
					PoloConfig.AES_KEY,
					PoloMethods.DELIVERY_ADDRESS_PDATE);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			// 调用三方接口
			ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, addressUpdateQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase deliveryInfo(HttpServletRequest request, @Valid @RequestBody ApiDeliveryInfoQuery deliveryInfoQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, deliveryInfoQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloDeliveryInfo(infoEntity,deliveryInfoQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 邮寄地址查询
	 * @param infoEntity
	 * @param deliveryInfoQuery
	 * @return
	 */
	public ResponseBase poloDeliveryInfo(MerchantsInfoEntity infoEntity, @Valid ApiDeliveryInfoQuery deliveryInfoQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(),
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY,
					PoloConfig.AES_KEY,
					PoloMethods.DELIVERY_INFO);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			// 调用三方接口
			ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, deliveryInfoQuery, config);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
