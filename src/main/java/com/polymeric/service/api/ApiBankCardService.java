package com.polymeric.service.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/bankcard")
@Api(value = "用户管理",tags = "用户管理")
public interface ApiBankCardService {
	
	@GetMapping("/merchant/card/list")
	@ApiOperation(value = "查询商户可用卡产品列表信息", notes = "查询商户可用卡产品列表信息", response = ResponseBase.class)
	ResponseBase merchantBankCardList(HttpServletRequest request);
	
	@GetMapping("/user/card/list")
	@ApiOperation(value = "用户卡列表信息", notes = "用户卡列表信息", response = ResponseBase.class)
	ResponseBase userCardList(HttpServletRequest request);

}
