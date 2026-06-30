package com.polymeric.service.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/webHookMsg")
@Api(value = "给商户回调消息",tags = "给商户回调消息")
public interface MerchantsWebHookMsgService {

	@PostMapping("/findList")
	@ApiOperation(value = "消息列表", notes = "消息列表", response = ResponseBase.class)
	ResponseBase findList(MerchantsWebHookMsgEntity entity);
	
	@GetMapping("/send")
	@ApiOperation(value = "手动执行回调", notes = "手动执行回调", response = ResponseBase.class)
	ResponseBase send(Integer id);
	
}
