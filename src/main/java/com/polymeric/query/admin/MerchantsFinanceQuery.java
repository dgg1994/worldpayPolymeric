package com.polymeric.query.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：商户充值请求
 *
 * @author GeminiSun
 * @date 2026/07/02 14:13
 */
@Data
public class MerchantsFinanceQuery {

    @ApiModelProperty(name = "mchId",value = "商户id",required = false,dataType = "mchId")
    private Integer mchId;

    @ApiModelProperty(name = "merchantsAddress",value = "商户充值信息",required = false,dataType = "merchantsAddress")
    private String merchantsAddress;

    @ApiModelProperty(name = "operator",value = "操作人",required = false,dataType = "operator")
    private String operator;

    @ApiModelProperty(name = "merchantsAmount",value = "商户充值金额",required = false,dataType = "merchantsAmount")
    private BigDecimal merchantsAmount;

    @ApiModelProperty(name = "remark",value = "商户充值备注",required = false,dataType = "remark")
    private String remark;
}