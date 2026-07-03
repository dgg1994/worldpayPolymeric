package com.polymeric.response.api;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 商户资产返回值
 * @author Hlin
 *
 */
@Data
public class MerchantBalanceRes {

	@ApiModelProperty(name = "currency", value = "币种", required = true, dataType = "String")
	private String currency;
	
	@ApiModelProperty(name = "availableAmount", value = "可有金额", required = true, dataType = "BigDecimal")
	private BigDecimal availableAmount;
	
	@ApiModelProperty(name = "frozenAmount", value = "冻结金额", required = true, dataType = "BigDecimal")
	private BigDecimal frozenAmount;
	
}
