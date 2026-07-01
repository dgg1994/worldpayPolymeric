package com.polymeric.service.admin;

import com.polymeric.entity.channel.ChannelCardEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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


	@PostMapping("/findList")
	@ApiOperation(value = "上游产品展示", notes = "上游产品展示", response = ResponseBase.class)
	ResponseBase findList(ChannelCardEntity entity);

	@PostMapping("/update")
	@ApiOperation(value = "编辑上游产品", notes = "编辑上游产品", response = ResponseBase.class)
	ResponseBase update(ChannelCardEntity entity);

	@GetMapping("/updateState")
	@ApiOperation(value = "编辑上游产品状态", notes = "编辑上游产品状态", response = ResponseBase.class)
	ResponseBase updateState(Integer id,Integer cardStatus);

}
