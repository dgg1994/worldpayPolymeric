package com.polymeric.entity.channel;
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
@TableName("channel_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "渠道基础信息",description = "渠道基础信息")
public class ChannelInfoEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("channel_code")
	@ApiModelProperty(name = "channelCode",value = "渠道编号",required = true,dataType = "String")
    private String channelCode;
	
	@TableField("channel_name")
	@ApiModelProperty(name = "channelName",value = "渠道名称",required = true,dataType = "String")
    private String channelName;
	
	@TableField("channel_state")
	@ApiModelProperty(name = "channelState",value = "渠道状态",required = true,dataType = "String")
    private Integer channelState;
	
	@TableField("private_key")
	@ApiModelProperty(name = "privateKey",value = "验证私钥",required = true,dataType = "String")
    private String privateKey;
	
	@TableField("app_Id")
	@ApiModelProperty(name = "appId",value = "appId",required = true,dataType = "String")
    private String appId;
	
	@TableField("aes_Key")
	@ApiModelProperty(name = "aesKe",value = "aesKe",required = true,dataType = "String")
    private String aesKe;
	
	@TableField("webhook_url")
	@ApiModelProperty(name = "webhookUrl",value = "回调地址",required = true,dataType = "String")
    private String webhookUrl;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	

}
