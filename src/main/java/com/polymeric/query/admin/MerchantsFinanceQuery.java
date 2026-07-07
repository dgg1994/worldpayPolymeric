package com.polymeric.query.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 类描述：商户充值请求
 *
 * @author GeminiSun
 * @date 2026/07/02 14:13
 */
@Data
public class MerchantsFinanceQuery {

	@NotNull(message = "商户id不能为空")
    @ApiModelProperty(name = "mchId",value = "商户id",required = false,dataType = "mchId")
    private Integer mchId;

	@NotBlank(message = "商户充值地址不能为空")
    @ApiModelProperty(name = "merchantsAddress",value = "商户充值信息",required = false,dataType = "merchantsAddress")
    private String merchantsAddress;

    @NotBlank(message = "商户充值地址类型不能为空")
    @ApiModelProperty(name = "addressType",value = "商户充值地址类型信息",required = false,dataType = "merchantsAddress")
    private String addressType;

	@NotNull(message = "充值金额不能为空")
    @ApiModelProperty(name = "merchantsAmount",value = "商户充值金额",required = false,dataType = "merchantsAmount")
    private BigDecimal merchantsAmount;

    @ApiModelProperty(name = "remark",value = "商户充值备注",required = false,dataType = "remark")
    private String remark;
}