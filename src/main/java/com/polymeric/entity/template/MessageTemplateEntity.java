package com.polymeric.entity.template;

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
@TableName("message_template")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "消息通知模板",description = "消息通知模板")
public class MessageTemplateEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = false,dataType = "Integer")
    private Integer id;
	
	@TableField("message_code")
	@ApiModelProperty(name = "messageCode",value = "消息编号",required = false,dataType = "Integer")
	private Integer messageCode;
	
	@TableField("language")
	@ApiModelProperty(name = "language",value = "语言分类",required = true,dataType = "String")
    private String language;
	
	@TableField("language_name")
	@ApiModelProperty(name = "languageName",value = "语言分类",required = true,dataType = "String")
    private String languageName;
	
	@TableField("msg_titles")
	@ApiModelProperty(name = "msgTitles",value = "消息标题",required = true,dataType = "String")
    private String msgTitles;
	
	@TableField("message_info")
	@ApiModelProperty(name = "messageInfo",value = "消息内容",required = true,dataType = "String")
    private String messageInfo;
	
	@TableField("message_url")
	@ApiModelProperty(name = "messageUrl",value = "透穿地址",required = true,dataType = "String")
    private String messageUrl;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
