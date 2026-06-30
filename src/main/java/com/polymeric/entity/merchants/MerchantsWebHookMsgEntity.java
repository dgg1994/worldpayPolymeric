package com.polymeric.entity.merchants;

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
@TableName("merchants_webhook_msg")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "给商户回调消息", description = "给商户回调消息")
public class MerchantsWebHookMsgEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "主键", required = true, dataType = "Integer")
	private Integer id;

	@TableField("msg_code")
	@ApiModelProperty(name = "msgCode", value = "消息编号", required = true, dataType = "String")
	private String msgCode;

	@TableField("mch_appid")
	@ApiModelProperty(name = "mchAppid", value = "商户appid", required = true, dataType = "String")
	private String mchAppid;

	@TableField("sys_orderno")
	@ApiModelProperty(name = "sysOrderNo", value = "平台订单号", required = true, dataType = "String")
	private String sysOrderNo;

	@TableField("merchant_order_no")
	@ApiModelProperty(name = "merchantOrderNo", value = "商户订单号", dataType = "String")
	private String merchantOrderNo;

	@TableField("callback_url")
	@ApiModelProperty(name = "callbackUrl", value = "回调地址", required = true, dataType = "String")
	private String callbackUrl;

	@TableField("callback_data")
	@ApiModelProperty(name = "callbackData", value = "回调数据", required = true, dataType = "String")
	private String callbackData;

	@TableField("status")
	@ApiModelProperty(name = "status", value = "状态：0-待回调，1-回调成功，2-回调失败，3-达到最大重试次数", dataType = "Integer")
	private Integer status;
	
	@TableField("retry_count")
	@ApiModelProperty(name = "retryCount", value = "已重试次数", dataType = "Integer")
	private Integer retryCount;
	
	@TableField("next_retry_time")
	@ApiModelProperty(name = "retryCount", value = "下次重试时间", dataType = "Date")
	private Date NextRetryTime;

	@TableField("setTime")
	@ApiModelProperty(name = "setTime", value = "注册时间", dataType = "Date")
	private Date setTime;

	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified", value = "更新时间", dataType = "Date")
	private Date gmtModified;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "statusName", value = "状态", dataType = "String")
	private String statusName;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "sysAccountId", value = "系统账号id", dataType = "String")
	private Integer sysAccountId;
}
