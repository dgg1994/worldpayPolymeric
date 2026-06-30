package com.polymeric.service.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.merchants.MerchantsInfoEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/merchants")
@Api(value = "商户管理",tags = "商户管理")
public interface MerchantsService {
	
	@PostMapping("/add")
	@ApiOperation(value = "新增商户", notes = "新增商户", response = ResponseBase.class)
	ResponseBase add(MerchantsInfoEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑商户", notes = "编辑商户", response = ResponseBase.class)
	ResponseBase update(MerchantsInfoEntity entity);
	
	@PostMapping("/findList")
	@ApiOperation(value = "商户列表", notes = "商户列表", response = ResponseBase.class)
	ResponseBase findList(MerchantsInfoEntity entity);
	
	@GetMapping("/updateState")
	@ApiOperation(value = "编辑商户状态", notes = "编辑商户状态", response = ResponseBase.class)
	ResponseBase updateState(Integer id,Integer merchantsStatus);
	
	@GetMapping("/updateKey")
	@ApiOperation(value = "更新商户密钥", notes = "更新商户密钥", response = ResponseBase.class)
	ResponseBase updateKey(Integer id);
	


}
