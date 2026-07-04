package com.polymeric.query.api;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 模拟退款参数
 * @author Hlin
 *
 */
@Data
public class ApiSimulationRefundQuery {
	
	@NotBlank(message = "交易id不能为空")
    @ApiModelProperty(name = "transactionId", value = "交易id", required = true, dataType = "Integer")
    private String transactionId;
	
	@NotNull(message = "交易金额不能为空")
    @ApiModelProperty(name = "transactionAmount", value = "交易金额", required = true, dataType = "Integer")
    private BigDecimal transactionAmount;
	
	@NotNull(message = "账单金额不能为空")
    @ApiModelProperty(name = "billAmount", value = "账单金额", required = true, dataType = "Integer")
    private BigDecimal billAmount;

}
