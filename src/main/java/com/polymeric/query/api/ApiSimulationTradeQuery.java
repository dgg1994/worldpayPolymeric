package com.polymeric.query.api;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApiSimulationTradeQuery {

	@NotNull(message = "银行卡id不能为空")
    @ApiModelProperty(name = "bankCardId", value = "银行卡id", required = true, dataType = "Integer")
    private Integer bankCardId;
	
	@NotNull(message = "交易金额不能为空")
    @ApiModelProperty(name = "transactionAmount", value = "交易金额", required = true, dataType = "Integer")
    private BigDecimal transactionAmount;
	
	@NotNull(message = "账单金额不能为空")
    @ApiModelProperty(name = "billAmount", value = "账单金额", required = true, dataType = "Integer")
    private BigDecimal billAmount;
	
	@NotBlank(message = "交易币种不能为空")
    @ApiModelProperty(name = "transactionCurrency", value = "交易币种", required = true, dataType = "Integer")
    private String transactionCurrency;
	
}
