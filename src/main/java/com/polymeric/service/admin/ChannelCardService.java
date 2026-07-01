package com.polymeric.service.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/channelCard")
@Api(value = "渠道银行卡",tags = "渠道银行卡")
public interface ChannelCardService {
	
	@GetMapping("/pull")
	@ApiOperation(value = "拉取对应渠道银行卡入口", notes = "拉取对应渠道银行卡入口", response = ResponseBase.class)
	ResponseBase pull(Integer id);

}
