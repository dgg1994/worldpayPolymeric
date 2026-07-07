package com.polymeric.response.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类描述：首页数据合集
 *
 * @author GeminiSun
 * @date 2026/07/06 11:21
 */
@Data
public class StatInfoRes {

    @ApiModelProperty(name = "channelMoney", value = "上游总金额", required = true, dataType = "BigDecimal")
    private BigDecimal channelMoney;

    @ApiModelProperty(name = "merchantMoney", value = "商户总金额", required = true, dataType = "BigDecimal")
    private BigDecimal merchantMoney;

    @ApiModelProperty(name = "totalFrozenAmount", value = "商户冻结总金额", required = true, dataType = "BigDecimal")
    private BigDecimal totalFrozenAmount;

    @ApiModelProperty(name = "transactionHistory", value = "交易流水", required = true, dataType = "BigDecimal")
    private BigDecimal transactionHistory;

    @ApiModelProperty(name = "channelCount", value = "上游总数", required = true, dataType = "Integer")
    private Integer channelCount;

    @ApiModelProperty(name = "merchantCount", value = "商户总数", required = true, dataType = "Integer")
    private Integer merchantCount;

    @ApiModelProperty(name = "callBackCount", value = "回调消息总数", required = true, dataType = "Long")
    private Long callBackCount;

    @ApiModelProperty(name = "apiRequestCount", value = "API访问次数", required = true, dataType = "Long")
    private Long apiRequestCount;

    @ApiModelProperty(name = "apiRequestCount", value = "出账金额", required = true, dataType = "Long")
    private Long chargeAmount;

    @ApiModelProperty(name = "apiRequestCount", value = "入账金额", required = true, dataType = "Long")
    private Long recordAmount;



}