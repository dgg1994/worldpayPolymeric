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
import com.polymeric.constants.Constants;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.pubapi.query.user.ApiRegisterQuery;
import com.polymeric.service.api.ApiUserService;
import com.polymeric.utils.I18nUtil;

@RestController
@Transactional
@CrossOrigin
public class ApiUserServiceImpl extends BaseApiService implements ApiUserService{
	
	@Autowired
	private ApiCheck apiCheck;

	@Override
	public ResponseBase register(HttpServletRequest request,@Valid @RequestBody ApiRegisterQuery registerQuery) {
		try {
			ResponseBase base = apiCheck.checkHeader(request, registerQuery);
			if(Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return null;
	}

}
