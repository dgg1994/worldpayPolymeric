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
import com.polymeric.dao.merchants.MerchantsUserDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.pubapi.query.user.ApiKycCountryQuery;
import com.polymeric.pubapi.query.user.ApiRegisterQuery;
import com.polymeric.response.polo.PoloRegisterRes;
import com.polymeric.service.api.ApiUserService;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.sign.ApiPoloUtil;

@RestController
@Transactional
@CrossOrigin
public class ApiUserServiceImpl extends BaseApiService implements ApiUserService{
	
	@Autowired
	private ApiCheck apiCheck;
	
	@Autowired
	private MerchantsUserDao merchantsUserDao;

	@Override
	public ResponseBase register(HttpServletRequest request,@Valid @RequestBody ApiRegisterQuery registerQuery) {
		try {
			ResponseBase base = apiCheck.checkHeader(request, registerQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloRegister(infoEntity,registerQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @param infoEntity 
	 * @param registerQuery 
	 * @category polo注册
	 * @return
	 */
	public ResponseBase poloRegister(MerchantsInfoEntity infoEntity, @Valid ApiRegisterQuery registerQuery) {
		try {
			//获取上游配置
			UnifiedConfig config= apiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL,
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY,
					PoloConfig.AES_KEY,
					PoloMethods.USER_REGISTER);
			//调用三方接口
			ResponseBase base = ApiPoloUtil.postData(null, null, registerQuery, config);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			PoloRegisterRes res = JSONObject.parseObject(JSON.toJSONString(base.getData()), PoloRegisterRes.class);
			//新增商户用户
			MerchantsUserEntity entity = new MerchantsUserEntity();
			entity.setApiUid(res.getUid());
			entity.setChannelCode(infoEntity.getChannelCode());
			entity.setChannelId(infoEntity.getChannelId());
			entity.setMchAppid(infoEntity.getAppId());
			entity.setMchId(infoEntity.getId());
			entity.setUserEmail(registerQuery.getEmail());
			entity.setUserTel(registerQuery.getTel());
			GenericityUtil.setDate(entity);
			merchantsUserDao.insert(entity);
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase kycCountryList(HttpServletRequest request,@Valid @RequestBody ApiKycCountryQuery kycCountryQuery) {
		try {
			ResponseBase base = apiCheck.checkHeader(request, kycCountryQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloKycCountryList(infoEntity,kycCountryQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo kyc国家列表查询
	 * @param infoEntity
	 * @param kycCountryQuery
	 * @return
	 */
	public ResponseBase poloKycCountryList(MerchantsInfoEntity infoEntity,@Valid ApiKycCountryQuery kycCountryQuery) {
		//获取上游配置
		UnifiedConfig config= apiCheck.getConfig(
				infoEntity.getChannelData(), 
				PoloConfig.API_URL,
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY,
				PoloConfig.AES_KEY,
				PoloMethods.KYC_COUNTRY_LIST);
		//调用三方接口
		ResponseBase base = ApiPoloUtil.postData(null, null, kycCountryQuery, config);
		return base;
	}

}
