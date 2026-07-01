package com.polymeric.entity.merchants;

import java.math.BigDecimal;

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
@TableName("merchants_card")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户开通银行卡信息",description = "商户开通银行卡信息")
public class MerchantsCardEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("card_title")
	@ApiModelProperty(name = "cardTitle",value = "卡标题名称",required = true,dataType = "String")
	private String cardTitle;
	
	@TableField("card_img")
	@ApiModelProperty(name = "cardImg",value = "银行卡图片",required = true,dataType = "String")
	private String cardImg;
	
	@TableField("mch_id")
	@ApiModelProperty(name = "mchId",value = "商户id",required = true,dataType = "String")
    private Integer mchId;
	
	@TableField("mch_appid")
	@ApiModelProperty(name = "mchAppid",value = "商户编号",required = true,dataType = "String")
    private String mchAppid;
	
	@TableField("channel_id")
	@ApiModelProperty(name = "channelId",value = "渠道id",required = true,dataType = "String")
    private Integer channelId;
	
	@TableField("channel_code")
	@ApiModelProperty(name = "channelCode",value = "渠道编号",required = true,dataType = "String")
    private String channelCode;
	
	@TableField("card_id")
	@ApiModelProperty(name = "cardId",value = "三方卡片id",required = true,dataType = "String")
	private Integer cardId;
	
	@TableField("card_bin")
	@ApiModelProperty(name = "cardBin",value = "关联BIN码",required = true,dataType = "String")
	private String cardBin;
	
	@TableField("bank_card_nature")
	@ApiModelProperty(name = "bankCardNature",value = "卡类型：VIRTUAL、PHYSICAL",required = true,dataType = "String")
	private String bankCardNature;
	
	@TableField("card_brand")
	@ApiModelProperty(name = "bankCardNature",value = "卡品牌",required = true,dataType = "String")
	private String cardBrand;
	
	@TableField("card_mode")
	@ApiModelProperty(name = "cardMode",value = "卡模式 NORMAL 常规卡 ;SHARE 共享卡",required = true,dataType = "String")
	private String cardMode;
	
	@TableField("ccy")
	@ApiModelProperty(name = "ccy",value = "卡消费币种",required = true,dataType = "String")
	private String ccy;
	
	@TableField("apply_fee")
	@ApiModelProperty(name = "applyFee",value = "开卡费用，开卡一次收取",required = true,dataType = "String")
	private BigDecimal applyFee;
	
	@TableField("recharge_fee")
	@ApiModelProperty(name = "rechargeFee",value = "充值手续费比例，0-1之间",required = true,dataType = "String")
	private BigDecimal rechargeFee;
	
	@TableField("bankcard_region")
	@ApiModelProperty(name = "bankcardRegion",value = "卡片区域",required = true,dataType = "String")
	private String bankcardRegion;
	
	@TableField("active_min_limit")
	@ApiModelProperty(name = "activeMinLimit",value = "虚拟卡激活首次充值最小金额",required = true,dataType = "String")
	private BigDecimal activeMinLimit;
	
	@TableField("recharge_min_limit")
	@ApiModelProperty(name = "rechargeMinLimit",value = "单笔充值最小金额",required = true,dataType = "String")
	private BigDecimal rechargeMinLimit;
	
	@TableField("card_state")
	@ApiModelProperty(name = "cardState",value = "上下架状态1正常2下架",required = true,dataType = "Integer")
	private Integer cardState;

}
