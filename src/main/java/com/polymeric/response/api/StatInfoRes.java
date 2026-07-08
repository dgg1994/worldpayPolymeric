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

    // ========== 上游相关 ==========
    @ApiModelProperty(name = "channelMoney", value = "上游总金额", required = true, dataType = "BigDecimal")
    private BigDecimal channelMoney;

    @ApiModelProperty(name = "channelCount", value = "上游总数", required = true, dataType = "Integer")
    private Integer channelCount;

    // ========== 商户相关 ==========
    @ApiModelProperty(name = "merchantCount", value = "商户总数", required = true, dataType = "Integer")
    private Integer merchantCount;

    @ApiModelProperty(name = "merchantRechargeTotal", value = "商户充值总额", required = true, dataType = "BigDecimal")
    private BigDecimal merchantRechargeTotal;

    @ApiModelProperty(name = "merchantMoney", value = "商户总金额", required = true, dataType = "BigDecimal")
    private BigDecimal merchantMoney;

    @ApiModelProperty(name = "totalFrozenAmount", value = "商户冻结总金额", required = true, dataType = "BigDecimal")
    private BigDecimal totalFrozenAmount;

    /** 银行卡充值总额 */
    @ApiModelProperty(name = "bankCardRechargeTotal", value = "银行卡充值总额", required = true, dataType = "BigDecimal")
    private BigDecimal bankCardRechargeTotal;

    /** 银行卡消费总额 */
    @ApiModelProperty(name = "bankCardConsumeTotal", value = "银行卡消费总额", required = true, dataType = "BigDecimal")
    private BigDecimal bankCardConsumeTotal;

    /** 银行卡开卡总额 */
    @ApiModelProperty(name = "bankCardOpenTotal", value = "银行卡开卡总额", required = true, dataType = "BigDecimal")
    private BigDecimal bankCardOpenTotal;

    /** 平台收益总额 */
    @ApiModelProperty(name = "platformIncomeTotal", value = "平台收益总额", required = true, dataType = "BigDecimal")
    private BigDecimal platformIncomeTotal;

    @ApiModelProperty(name = "transactionHistory", value = "交易流水", required = true, dataType = "BigDecimal")
    private BigDecimal transactionHistory;

    @ApiModelProperty(name = "chargeAmount", value = "出账金额", required = true, dataType = "Long")
    private Long chargeAmount;

    @ApiModelProperty(name = "recordAmount", value = "入账金额", required = true, dataType = "Long")
    private Long recordAmount;

    // ========== 用户相关 ==========
    @ApiModelProperty(name = "userRegisterTotal", value = "用户注册总量", required = true, dataType = "Long")
    private Long userRegisterTotal;

    @ApiModelProperty(name = "bankCardOpenCount", value = "银行卡开卡总量", required = true, dataType = "Long")
    private Long bankCardOpenCount;

    @ApiModelProperty(name = "bankCardActiveCount", value = "银行卡激活总量", required = true, dataType = "Long")
    private Long bankCardActiveCount;

    // ========== API相关 ==========
    @ApiModelProperty(name = "callBackCount", value = "回调消息总数", required = true, dataType = "Long")
    private Long callBackCount;



}