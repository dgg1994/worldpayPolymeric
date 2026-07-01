package com.polymeric.service.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.query.api.ApiActiveQuery;
import com.polymeric.query.api.ApiCardApplyQuery;
import com.polymeric.query.api.ApiGetCanActiveQuery;
import com.polymeric.query.api.ApiSetPinQuery;

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
	
	@PostMapping("/apply")
	@ApiOperation(value = "申请银行卡", notes = "申请银行卡", response = ResponseBase.class)
	ResponseBase applyCard(HttpServletRequest request,ApiCardApplyQuery cardApplyQuery);

	@PostMapping("/get/canActive")
	@ApiOperation(value = "银行卡是否可激活", notes = "银行卡是否可激活", response = ResponseBase.class)
	ResponseBase getCanActive(HttpServletRequest request,ApiGetCanActiveQuery activeQuery);
	
	@PostMapping("/active")
	@ApiOperation(value = "银行卡激活", notes = "银行卡激活", response = ResponseBase.class)
	ResponseBase active(HttpServletRequest request,ApiActiveQuery activeQuery);
	
	@PostMapping("/setPin")
	@ApiOperation(value = "设置Pin", notes = "设置Pin", response = ResponseBase.class)
	ResponseBase setPin(HttpServletRequest request,ApiSetPinQuery setPinQuery);
	
}
