package com.polymeric.service.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/merchant")
@Api(value = "商户",tags = "商户")
public interface ApiMerchantService {
	
	@GetMapping("/balance")
	@ApiOperation(value = "查询商户资产", notes = "查询商户资产", response = ResponseBase.class)
	ResponseBase merchantBalance(HttpServletRequest request);

}
