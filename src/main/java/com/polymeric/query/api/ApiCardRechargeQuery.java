package com.polymeric.query.api;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @category 银行卡充值
 * @author Hlin
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiCardRechargeQuery extends ApiBankCardIdQuery{
	
	@NotNull(message = "充值金额不能为空")
    @ApiModelProperty(name = "amount", value = "充值金额", required = true, dataType = "BigDecimal")
    private BigDecimal amount;
	
	@NotBlank(message = "充值订单号")
    @ApiModelProperty(name = "requestOrderId", value = "充值订单号", required = true, dataType = "String")
    private String requestOrderId;
	
//    @ApiModelProperty(name = "sysOrderNum", value = "平台订单号", required = false, dataType = "string")
//    private String sysOrderNum;

}
