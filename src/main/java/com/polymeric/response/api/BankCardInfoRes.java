package com.polymeric.response.api;

import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BankCardInfoRes {
	
	@ApiModelProperty(name = "id", value = "交易记录id", required = true, dataType = "Integer")
	private Integer id;
	
	@ApiModelProperty(name = "orderNum", value = "订单号", required = true, dataType = "String")
	private String orderNum;
	
	@ApiModelProperty(name = "mchAppid",value = "商户编号",required = true,dataType = "String")
    private String mchAppid;
	
	@ApiModelProperty(name = "userUid",value = "商户用户uid",required = true,dataType = "String")
    private String userUid;
	
	@ApiModelProperty(name = "userBankcardId", value = "用户银行卡唯一id", required = true, dataType = "Integer")
	private Integer userBankcardId;

	@ApiModelProperty(name = "tradeType",value = "交易类型:170卡片消费、174卡预授权、171卡片退款、172卡片手续费、173卡片ATM取现、140申请卡片、150卡片充值、175卡片注销",required = true,dataType = "String")
    private Integer tradeType;
	
	@ApiModelProperty(name = "orderAmount",value = "订单金额",required = true,dataType = "BigDecimal")
    private BigDecimal orderAmount;
	
	@ApiModelProperty(name = "enterAmount",value = "入账金额",required = true,dataType = "BigDecimal")
    private BigDecimal enterAmount;
	
	@ApiModelProperty(name = "feeAmount",value = "手续费金额",required = true,dataType = "BigDecimal")
    private BigDecimal feeAmount;
	
	@ApiModelProperty(name = "orderCurrency",value = "订单币种",required = true,dataType = "String")
    private String orderCurrency;
	
	@ApiModelProperty(name = "feeCurrency",value = "手续费币种",required = true,dataType = "String")
    private String feeCurrency;
	
	@ApiModelProperty(name = "transCurrency",value = "交易币种",required = true,dataType = "String")
    private String transCurrency;
	
	@ApiModelProperty(name = "orderState",value = "订单状态 0初始化、1处理中、2成功、3失败",required = true,dataType = "Integer")
    private Integer orderState;
	
	@ApiModelProperty(name = "orderRemark",value = "订单备注",required = true,dataType = "String")
    private String orderRemark;
	
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	
}
