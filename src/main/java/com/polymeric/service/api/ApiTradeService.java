package com.polymeric.service.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.query.api.ApiAuthApproveQuery;
import com.polymeric.query.api.ApiRecordInfoQuery;
import com.polymeric.query.api.ApiRecordQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/trade")
@Api(value = "交易",tags = "交易")
public interface ApiTradeService {
	
	@PostMapping("/record")
	@ApiOperation(value = "查询银行卡交易记录", notes = "查询银行卡交易记录", response = ResponseBase.class)
	ResponseBase record(HttpServletRequest request,ApiRecordQuery recordQuery);
	
	@PostMapping("/record/info")
	@ApiOperation(value = "银行卡交易记录详情", notes = "银行卡交易记录详情", response = ResponseBase.class)
	ResponseBase recordInfo(HttpServletRequest request,ApiRecordInfoQuery infoQuery);
	
	@PostMapping("/3ds/approve")
	@ApiOperation(value = "3ds授权通过", notes = "3ds授权通过", response = ResponseBase.class)
	ResponseBase authApprove(HttpServletRequest request,ApiAuthApproveQuery authApproveQuery);
	
	@PostMapping("/3ds/reject")
	@ApiOperation(value = "3ds授权拒绝", notes = "3ds授权拒绝", response = ResponseBase.class)
	ResponseBase authReject(HttpServletRequest request,ApiAuthApproveQuery authApproveQuery);

}
