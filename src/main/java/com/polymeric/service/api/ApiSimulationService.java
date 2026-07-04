package com.polymeric.service.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.query.api.ApiSimulationAuthorizeQuery;
import com.polymeric.query.api.ApiSimulationRefundQuery;
import com.polymeric.query.api.ApiSimulationReversalQuery;
import com.polymeric.query.api.ApiSimulationSettlementQuery;
import com.polymeric.query.api.ApiSimulationTradeQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/simulation")
@Api(value = "模拟交易",tags = "模拟交易")
public interface ApiSimulationService {
	
	@PostMapping("/3ds")
	@ApiOperation(value = "模拟3ds", notes = "模拟3ds", response = ResponseBase.class)
	ResponseBase simulationAuthorize(HttpServletRequest request,ApiSimulationAuthorizeQuery authorizeQuery);
	
	@PostMapping("/trade")
	@ApiOperation(value = "模拟交易", notes = "模拟交易", response = ResponseBase.class)
	ResponseBase simulationTrade(HttpServletRequest request,ApiSimulationTradeQuery tradeQuery);
	
	@PostMapping("/refund")
	@ApiOperation(value = "模拟退款", notes = "模拟退款", response = ResponseBase.class)
	ResponseBase simulationRefund(HttpServletRequest request,ApiSimulationRefundQuery refundQuery);
	
	@PostMapping("/settlement")
	@ApiOperation(value = "模拟结算", notes = "模拟结算", response = ResponseBase.class)
	ResponseBase simulationSettlement(HttpServletRequest request,ApiSimulationSettlementQuery settlementQuery);
	
	@PostMapping("/reversal")
	@ApiOperation(value = "模拟退单", notes = "模拟退单", response = ResponseBase.class)
	ResponseBase simulationReversal(HttpServletRequest request,ApiSimulationReversalQuery reversalQuery);

}
