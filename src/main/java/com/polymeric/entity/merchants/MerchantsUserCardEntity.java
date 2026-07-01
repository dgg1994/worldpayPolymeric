package com.polymeric.entity.merchants;

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
@TableName("merchants_user_card")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户用户银行卡信息", description = "商户用户银行卡信息")
public class MerchantsUserCardEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "主键", required = true, dataType = "Integer")
	private Integer id;
	
	@TableField("user_id")
	@ApiModelProperty(name = "userId", value = "用户id", required = true, dataType = "String")
	private Integer userId;
	
	@TableField("user_uid")
	@ApiModelProperty(name = "userUid", value = "用户uid", required = true, dataType = "String")
	private String userUid;

	@TableField("mch_id")
	@ApiModelProperty(name = "mchId", value = "商户id", required = true, dataType = "String")
	private Integer mchId;

	@TableField("mch_appid")
	@ApiModelProperty(name = "mchAppid", value = "商户APPID", required = true, dataType = "String")
	private String mchAppid;
	
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

	@TableField("card_num")
	@ApiModelProperty(name = "cardNum", value = "银行卡号", required = true, dataType = "String")
	private String cardNum;
	
	@TableField("card_balance")
	@ApiModelProperty(name = "cardBalance", value = "银行卡余额", required = true, dataType = "BigDecimal")
	private BigDecimal cardBalance;
	
	@TableField("card_state")
	@ApiModelProperty(name = "cardState", value = "银行卡状态", required = true, dataType = "Integer")
	private Integer cardState;
	
	@TableField("pin_num")
	@ApiModelProperty(name = "pinNum", value = "pin码", required = true, dataType = "String")
	private String pinNum;
	
	@TableField("order_num")
	@ApiModelProperty(name = "orderNum", value = "订单号", required = true, dataType = "String")
	private String orderNum;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
}
