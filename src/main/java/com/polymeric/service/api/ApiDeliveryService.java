package com.polymeric.service.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.query.api.ApiAddressAddQuery;
import com.polymeric.query.api.ApiAddressUpdateQuery;
import com.polymeric.query.api.ApiDeliveryInfoQuery;
import com.polymeric.query.api.ApiDeliveryRegionQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/delivery")
@Api(value = "邮寄",tags = "邮寄")
public interface ApiDeliveryService {
	
	@PostMapping("/region")
	@ApiOperation(value = "查询邮寄地区列表", notes = "查询邮寄地区列表", response = ResponseBase.class)
	ResponseBase deliveryRegion(HttpServletRequest request,ApiDeliveryRegionQuery regionQuery);
	
	@PostMapping("/address/add")
	@ApiOperation(value = "添加邮寄地址", notes = "添加邮寄地址", response = ResponseBase.class)
	ResponseBase addressAdd(HttpServletRequest request,ApiAddressAddQuery addressAddQuery);
	
	@PostMapping("/address/update")
	@ApiOperation(value = "更新邮寄地址", notes = "更新邮寄地址", response = ResponseBase.class)
	ResponseBase addressUpdate(HttpServletRequest request,ApiAddressUpdateQuery addressUpdateQuery);
	
	@PostMapping("/info")
	@ApiOperation(value = "查询邮寄信息", notes = "查询邮寄信息邮寄地址", response = ResponseBase.class)
	ResponseBase deliveryInfo(HttpServletRequest request,ApiDeliveryInfoQuery deliveryInfoQuery);

}
