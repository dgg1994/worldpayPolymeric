package com.polymeric.service.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/dic")
@Api(value = "字典",tags = "字典")
public interface DicService {

	@GetMapping("/getLanguage")
	@ApiOperation(value = "查询语言列表",notes = "查询语言列表",response = ResponseBase.class)
	ResponseBase getLanguage();
	
	@GetMapping("/findUserState")
	@ApiOperation(value = "查询用户状态",notes = "查询用户状态",response = ResponseBase.class)
	ResponseBase findUserState();

	@GetMapping("/findKycState")
	@ApiOperation(value = "查询用户KYC状态",notes = "查询用户KYC状态",response = ResponseBase.class)
	ResponseBase findKycState();
	
	@GetMapping("/findDeviceType")
	@ApiOperation(value = "查询设备类型",notes = "查询设备类型",response = ResponseBase.class)
	ResponseBase findDeviceType();
	
	@GetMapping("/findOrderState")
	@ApiOperation(value = "查询订单状态",notes = "查询订单状态",response = ResponseBase.class)
	ResponseBase findOrderState();

	@GetMapping("/findOrderTradeType")
	@ApiOperation(value = "查询商户资金交易类型",notes = "查询商户资金交易类型",response = ResponseBase.class)
	ResponseBase findOrderTradeType();

	@GetMapping("/findTradeType")
	@ApiOperation(value = "查询商户交易类型",notes = "查询商户交易类型",response = ResponseBase.class)
	ResponseBase findTradeType();

	@GetMapping("/findOrderType")
	@ApiOperation(value = "查询订单交易类型",notes = "查询订单交易类型",response = ResponseBase.class)
	ResponseBase findOrderType();

	@GetMapping("/findWebhookPoloType")
	@ApiOperation(value = "查询商户回调类型",notes = "查询商户回调类型",response = ResponseBase.class)
	ResponseBase findWebhookPoloType();

	@GetMapping("/findCardState")
	@ApiOperation(value = "查询银行卡状态",notes = "查询银行卡状态",response = ResponseBase.class)
	ResponseBase findCardState();

	

}
