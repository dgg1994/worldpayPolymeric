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
	
	@TableField("apply_discount")
	@ApiModelProperty(value = "申请折扣，实际申请费用 = applyDiscount * applyFee")
	private Integer applyDiscount;

	@TableField("apply_fee")
	@ApiModelProperty(value = "申请费用（美元），每次申请卡时从商户基本账户扣除")
	private BigDecimal applyFee;

	@TableField("bankcard_nature")
	@ApiModelProperty(value = "卡片性质：PHYSICAL（实体卡）或 VIRTUAL（虚拟卡）")
	private String bankCardNature;

	@TableField("bankcard_source")
	@ApiModelProperty(value = "银行卡来源")
	private String bankCardSource;

	@TableField("bankcard_type")
	@ApiModelProperty(value = "卡片类型：MASTER 或 VISA")
	private String bankCardType;
	
	@TableField("card_brand")
	@ApiModelProperty(value = "卡片类型：MASTER 或 VISA")
	private String cardBrand;

	@TableField("category_id")
	@ApiModelProperty(value = "分类ID")
	private Integer categoryId;

	@TableField("card_ccy")
	@ApiModelProperty(value = "卡币种，例如 USD")
	private String cardCcy;

	@TableField("card_description1")
	@ApiModelProperty(value = "卡片描述1")
	private String cardDescription1;

	@TableField("card_description2")
	@ApiModelProperty(value = "卡片描述2")
	private String cardDescription2;

	@TableField("card_enable")
	@ApiModelProperty(value = "是否允许申请")
	private Boolean cardEnable;

	@TableField("card_hot")
	@ApiModelProperty(value = "是否热门卡片")
	private Boolean cardHot;
	
	@TableField("card_img")
	@ApiModelProperty(value = "卡片展示图片URL")
	private String cardImg;
	
	@TableField("card_list_img")
	@ApiModelProperty(value = "卡片列表展示图片URL")
	private String cardListImg;

	@TableField("month_fee")
	@ApiModelProperty(value = "月费（美元），保留字段")
	private BigDecimal monthFee;

	@TableField("recharge_fee")
	@ApiModelProperty(value = "充值手续费率，例如 0.01 表示 1%")
	private BigDecimal rechargeFee;

	@TableField("refund_fee")
	@ApiModelProperty(value = "退款手续费（必填）")
	private BigDecimal refundFee;

	@TableField("recommend")
	@ApiModelProperty(value = "是否推荐")
	private Boolean recommend;

	@TableField("sort_param")
	@ApiModelProperty(value = "排序参数")
	private Integer sortParam;

	@TableField("card_title")
	@ApiModelProperty(value = "卡片名称")
	private String cardTitle;

	@TableField("active_min_limit")
	@ApiModelProperty(value = "激活/首充最低金额（必填）")
	private BigDecimal activeMinLimit;

	@TableField("recharge_min_limit")
	@ApiModelProperty(value = "每次非首充最低金额（必填）")
	private BigDecimal rechargeMinLimit;

	@TableField("recharge_max_limit")
	@ApiModelProperty(value = "每次非首充最高金额（必填）")
	private BigDecimal rechargeMaxLimit;

	@TableField("card_bin")
	@ApiModelProperty(value = "卡号段（必填）")
	private String cardBin;
	
	@TableField("open_card_cost")
	@ApiModelProperty(value = "开卡费用")
	private BigDecimal openCardCost;
	
	@TableField("pre_save_cost")
	@ApiModelProperty(value = "预存费用")
	private BigDecimal preSaveCost;
	
	@TableField("card_state")
	@ApiModelProperty(name = "cardState",value = "上下架状态1正常2下架",required = true,dataType = "Integer")
	private Integer cardState;

}
