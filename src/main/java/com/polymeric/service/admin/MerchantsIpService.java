package com.polymeric.service.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.merchants.MerchantsIpEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/merchantsIp")
@Api(value = "商户白名单管理",tags = "商户白名单管理")
public interface MerchantsIpService {
	
	@PostMapping("/findList")
	@ApiOperation(value = "商户ip白名单列表", notes = "商户ip白名单列表", response = ResponseBase.class)
	ResponseBase findList(MerchantsIpEntity entity);

	@PostMapping("/addOrUpIpWhitelist")
	@ApiOperation(value = "新增编辑商户白名单", notes = "新增商户白名单", response = ResponseBase.class)
	ResponseBase addOrUpIpWhitelist(MerchantsIpEntity entity);
	
	@PostMapping("/updateIp")
	@ApiOperation(value = "修改商户白名单", notes = "修改商户白名单", response = ResponseBase.class)
	ResponseBase updateIp(MerchantsIpEntity entity);
	
	@PostMapping("/deleteIp")
	@ApiOperation(value = "删除商户白名单", notes = "删除商户白名单", response = ResponseBase.class)
	ResponseBase deleteIp(Integer id);
	
}
