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
@TableName("order_sys_income_list")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "平台收益订单记录",description = "平台收益订单记录")
public class OrderIncomeListEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("order_num")
	@ApiModelProperty(name = "orderNum", value = "订单号", required = true, dataType = "String")
	private String orderNum;
	
	@TableField("channel_id")
	@ApiModelProperty(name = "channelId",value = "渠道id",required = true,dataType = "String")
    private Integer channelId;
	
	@TableField("channel_code")
	@ApiModelProperty(name = "channelCode",value = "渠道编号",required = true,dataType = "String")
    private String channelCode;
	
	@TableField("mch_id")
	@ApiModelProperty(name = "mchId",value = "商户id",required = true,dataType = "String")
    private Integer mchId;
	
	@TableField("mch_appid")
	@ApiModelProperty(name = "mchAppid",value = "商户编号",required = true,dataType = "String")
    private String mchAppid;
	
	@TableField("user_id")
	@ApiModelProperty(name = "userId",value = "商户用户id",required = true,dataType = "String")
    private Integer userId;
	
	@TableField("user_uid")
	@ApiModelProperty(name = "userUid",value = "商户用户uid",required = true,dataType = "String")
    private String userUid;
	
	@TableField("card_id")
	@ApiModelProperty(name = "card_id", value = "银行卡id", required = true, dataType = "String")
	private Integer cardId;
	
	@TableField("card_api_id")
	@ApiModelProperty(name = "cardApiId", value = "三方银行卡id", required = true, dataType = "String")
	private Integer cardApiId;
	
	@TableField("user_bankcard_id")
	@ApiModelProperty(name = "userBankcardId", value = "用户银行卡唯一id", required = true, dataType = "Integer")
	private Integer userBankcardId;
	
	@TableField("card_type")
	@ApiModelProperty(name = "cardType", value = "银行卡类型", required = true, dataType = "String")
	private String cardType;
	
	@TableField("order_type")
	@ApiModelProperty(name = "orderType", value = "订单类型", required = true, dataType = "String")
	private Integer orderType;
	
	@TableField("order_amount")
	@ApiModelProperty(name = "orderAmount", value = "订单金额", required = true, dataType = "BigDecimal")
	private BigDecimal orderAmount;

	@TableField("channel_amount")
	@ApiModelProperty(name = "channelAmount", value = "渠道费用", required = true, dataType = "BigDecimal")
	private BigDecimal channelAmount;
	
	@TableField("channel_rates")
	@ApiModelProperty(name = "channelRates", value = "渠道费率", required = true, dataType = "BigDecimal")
	private BigDecimal channelRates;
	
	@TableField("sys_amount")
	@ApiModelProperty(name = "sysAmount", value = "平台费用", required = true, dataType = "BigDecimal")
	private BigDecimal sysAmount;
	
	@TableField("sys_rates")
	@ApiModelProperty(name = "sysRates", value = "平台费率", required = true, dataType = "BigDecimal")
	private BigDecimal sysRates;
	
	@TableField("profit_amount")
	@ApiModelProperty(name = "profitAmount", value = "利润", required = true, dataType = "String")
	private BigDecimal profitAmount;
	
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
