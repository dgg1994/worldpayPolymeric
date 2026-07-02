package com.polymeric.entity.order;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.polymeric.query.pub.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("order_bankcard_trade_list")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "银行卡交易记录",description = "银行卡交易记录")
public class OrderBankCardTradeEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("order_num")
	@ApiModelProperty(name = "orderNum", value = "订单号", required = true, dataType = "String")
	private String orderNum;
	
	@TableField("mch_order_num")
	@ApiModelProperty(name = "mchOrderNum", value = "下游商户订单号", required = true, dataType = "String")
	private String mchOrderNum;
	
	@TableField("mch_appid")
	@ApiModelProperty(name = "mchAppid",value = "商户编号",required = true,dataType = "String")
    private String mchAppid;
	
	@TableField("user_uid")
	@ApiModelProperty(name = "userUid",value = "商户用户uid",required = true,dataType = "String")
    private String userUid;
	
	@TableField("user_bankcard_id")
	@ApiModelProperty(name = "userBankcardId", value = "用户银行卡唯一id", required = true, dataType = "Integer")
	private Integer userBankcardId;

	@TableField("trade_type")
	@ApiModelProperty(name = "tradeType",value = "交易类型:170卡片消费、174卡预授权、171卡片退款、172卡片手续费、173卡片ATM取现、140申请卡片、150卡片充值、175卡片注销",required = true,dataType = "String")
    private Integer tradeType;
	
	@TableField("order_amount")
	@ApiModelProperty(name = "orderAmount",value = "订单金额",required = true,dataType = "BigDecimal")
    private BigDecimal orderAmount;
	
	@TableField("enter_amount")
	@ApiModelProperty(name = "enterAmount",value = "入账金额",required = true,dataType = "BigDecimal")
    private BigDecimal enterAmount;
	
	@TableField("fee_amount")
	@ApiModelProperty(name = "feeAmount",value = "手续费金额",required = true,dataType = "BigDecimal")
    private BigDecimal feeAmount;
	
	@TableField("order_currency")
	@ApiModelProperty(name = "orderCurrency",value = "订单币种",required = true,dataType = "String")
    private String orderCurrency;
	
	@TableField("fee_currency")
	@ApiModelProperty(name = "feeCurrency",value = "手续费币种",required = true,dataType = "String")
    private String feeCurrency;
	
	@TableField("trans_currency")
	@ApiModelProperty(name = "transCurrency",value = "交易币种",required = true,dataType = "String")
    private String transCurrency;
	
	@TableField("order_state")
	@ApiModelProperty(name = "orderState",value = "订单状态 0初始化、1处理中、2成功、3失败",required = true,dataType = "Integer")
    private Integer orderState;
	
	@TableField("order_remark")
	@ApiModelProperty(name = "orderRemark",value = "订单备注",required = true,dataType = "String")
    private String orderRemark;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	
}
