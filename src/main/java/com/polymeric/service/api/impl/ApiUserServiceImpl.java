package com.polymeric.service.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import com.polymeric.dao.merchants.MerchantsUserKycDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserEntity;
import com.polymeric.entity.merchants.MerchantsUserKycEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.enums.KycStateEnums;
import com.polymeric.query.api.ApiKycApplyQuery;
import com.polymeric.query.api.ApiKycCountryQuery;
import com.polymeric.query.api.ApiRegisterQuery;
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
	private MerchantsUserDao merchantsUserDao;
	
	@Autowired
	private MerchantsUserKycDao merchantsUserKycDao;

	@Override
	public ResponseBase register(HttpServletRequest request,@Valid @RequestBody ApiRegisterQuery registerQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, registerQuery);
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
			UnifiedConfig config= ApiCheck.getConfig(
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
			entity.setKycState(KycStateEnums.WAIT_APPROVE.getIndex());
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
			ResponseBase base = ApiCheck.checkHeader(request, kycCountryQuery);
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
		UnifiedConfig config= ApiCheck.getConfig(
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

	@Override
	public ResponseBase kycStatus(HttpServletRequest request) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, null);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.polokycStatus(infoEntity);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public ResponseBase polokycStatus(MerchantsInfoEntity infoEntity) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(), 
				PoloConfig.API_URL, 
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.KYC_STATUS);
		if (infoEntity.getMerchantsUserData() == null) {
		    return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 调用三方接口
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, null, config);
		return base;
	}

	@Override
	public ResponseBase kycApply(HttpServletRequest request,@Valid @RequestBody ApiKycApplyQuery apiKycApplyQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, apiKycApplyQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloKycApply(infoEntity,apiKycApplyQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo kyc信息提交
	 * @param infoEntity
	 * @param apiKycApplyQuery
	 * @return
	 */
	public ResponseBase poloKycApply(MerchantsInfoEntity infoEntity, @Valid ApiKycApplyQuery apiKycApplyQuery) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(), 
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY,
					PoloConfig.AES_KEY, 
					PoloMethods.KYC_APPLY);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			// 调用三方接口
			ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, apiKycApplyQuery, config);
			if(Constants.HTTP_RES_CODE_200.equals(base.getCode())) {//提交成功
				MerchantsUserEntity userEntity = infoEntity.getMerchantsUserData();
				MerchantsUserKycEntity entity = new MerchantsUserKycEntity();
				BeanUtils.copyProperties(apiKycApplyQuery, entity);
				entity.setId(null);
				entity.setMchId(infoEntity.getId());
				entity.setMchAppid(infoEntity.getAppId());
				entity.setUserEmail(apiKycApplyQuery.getEmail());
				entity.setUserBirthday(apiKycApplyQuery.getBirthday());
				entity.setUserPhone(apiKycApplyQuery.getPhone());
				entity.setUserId(userEntity.getId());
				entity.setUserUid(userEntity.getApiUid());
				GenericityUtil.setDate(entity);
				merchantsUserKycDao.insert(entity);
				userEntity.setKycState(KycStateEnums.PROCESS_APPROVE.getIndex());
				merchantsUserDao.updateById(userEntity);
			}
			return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
