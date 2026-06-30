package com.polymeric.entity.merchants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.query.pub.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @category 商户基础信息
 * @author Hlin
 *
 */
@Data
@TableName("merchants_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户基础信息",description = "商户基础信息")
public class MerchantsInfoEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("sys_account_id")
	@ApiModelProperty(name = "sysAccountId",value = "系统账户id",required = true,dataType = "String")
    private Integer sysAccountId;
	
	@TableField("app_id")
	@ApiModelProperty(name = "appId",value = "APPID",required = true,dataType = "String")
    private String appId;
	
	@TableField("channel_id")
	@ApiModelProperty(name = "channel_id",value = "绑定渠道id",required = true,dataType = "String")
    private Integer channelId;
	
	@TableField("channel_code")
	@ApiModelProperty(name = "channelCode",value = "绑定渠道编号",required = true,dataType = "String")
    private String channelCode;
	
	@TableField("merchants_namme")
	@ApiModelProperty(name = "merchantsNamme",value = "商户名称",required = true,dataType = "String")
    private String merchantsNamme;
	
	@TableField("merchants_account")
	@ApiModelProperty(name = "merchantsAccount",value = "商户账号",required = true,dataType = "String")
    private String merchantsAccount;
	
	@TableField("contact_phone")
	@ApiModelProperty(name = "contactPhone",value = "手机号",required = true,dataType = "String")
    private String contactPhone;
	
	@TableField("available_amount")
	@ApiModelProperty(name = "availableAmount",value = "可用余额",required = true,dataType = "String")
    private BigDecimal availableAmount;
	
	@TableField("merchants_status")
	@ApiModelProperty(name = "merchantsStatus",value = "商户状态",required = true,dataType = "Integer")
    private Integer merchantsStatus;
	
	@TableField("webhook_url")
	@ApiModelProperty(name = "webhookUrl",value = "回调地址",required = true,dataType = "String")
    private String webhookUrl;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "ipList",value = "ip白名单",required = false,dataType = "List<MerchantsIpEntity>")
	private List<MerchantsIpEntity> ipList;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "merchantsKey",value = "商户密钥配置",required = false,dataType = "MerchantsKeyEntity")
	private MerchantsKeyEntity merchantsKey;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "channelData",value = "绑定上游渠道信息",required = false,dataType = "channelData")
	private ChannelInfoEntity channelData;
	
}
