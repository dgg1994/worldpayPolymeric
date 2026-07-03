package com.polymeric.service.api.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.response.api.MerchantBalanceRes;
import com.polymeric.service.api.ApiMerchantService;
import com.polymeric.utils.I18nUtil;

@RestController
@Transactional
@CrossOrigin
public class ApiMerchantServiceImpl extends BaseApiService implements ApiMerchantService{
	
	@Override
	public ResponseBase merchantBalance(HttpServletRequest request) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, null);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloMerchantBalance(infoEntity);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public ResponseBase poloMerchantBalance(MerchantsInfoEntity infoEntity) {
		try {
			MerchantBalanceRes base = new MerchantBalanceRes();
			base.setCurrency(Constants.USD);
			base.setAvailableAmount(infoEntity.getAvailableAmount());
			base.setFrozenAmount(infoEntity.getFreezeAmount());
			return setResultSuccess(base);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
