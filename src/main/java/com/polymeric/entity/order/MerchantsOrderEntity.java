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
@TableName("order_mch_cash_flow")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户资金流水",description = "商户资金流水")
public class MerchantsOrderEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("order_num")
	@ApiModelProperty(name = "orderNum", value = "订单号", required = true, dataType = "String")
	private String orderNum;
	
	@TableField("mch_id")
	@ApiModelProperty(name = "mchId",value = "商户id",required = true,dataType = "String")
    private Integer mchId;
	
	@TableField("mch_appid")
	@ApiModelProperty(name = "mchAppid",value = "商户编号",required = true,dataType = "String")
    private String mchAppid;
	
	@TableField("trade_type")
	@ApiModelProperty(name = "tradeType",value = "交易类型（1入账；2出账）",required = true,dataType = "String")
    private Integer tradeType;
	
	@TableField("order_type")
	@ApiModelProperty(name = "orderType",value = "订单类型",required = true,dataType = "String")
    private Integer orderType;
	
	@TableField("order_amount")
	@ApiModelProperty(name = "orderAmount",value = "订单金额",required = true,dataType = "String")
    private BigDecimal orderAmount;
	
	@TableField("actual_amount")
	@ApiModelProperty(name = "actualAmount",value = "实际金额",required = true,dataType = "String")
    private BigDecimal actualAmount;
	
	@TableField("before_amount")
	@ApiModelProperty(name = "beforeAmount",value = "交易前余额",required = true,dataType = "String")
    private BigDecimal beforeAmount;
	
	@TableField("after_amount")
	@ApiModelProperty(name = "afterAmount",value = "交易后余额",required = true,dataType = "String")
    private BigDecimal afterAmount;
	
	@TableField("order_state")
	@ApiModelProperty(name = "orderState", value = "订单状态", required = true, dataType = "Integer")
	private Integer orderState;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
