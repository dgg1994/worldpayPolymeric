package com.polymeric.service.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.pubapi.query.user.ApiKycCountryQuery;
import com.polymeric.pubapi.query.user.ApiRegisterQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/user")
@Api(value = "用户管理",tags = "用户管理")
public interface ApiUserService {
	
	@PostMapping("/register")
	@ApiOperation(value = "用户注册", notes = "用户注册", response = ResponseBase.class)
	ResponseBase register(HttpServletRequest request, ApiRegisterQuery registerQuery);
	
	@PostMapping("/kyc/country/list")
	@ApiOperation(value = "KYC国家列表", notes = "KYC国家列表", response = ResponseBase.class)
	ResponseBase kycCountryList(HttpServletRequest request, ApiKycCountryQuery kycCountryQuery);
	
	@GetMapping("/kyc/status")
	@ApiOperation(value = "查询KYC状态", notes = "查询KYC状态", response = ResponseBase.class)
	ResponseBase kycStatus(HttpServletRequest request);

}
