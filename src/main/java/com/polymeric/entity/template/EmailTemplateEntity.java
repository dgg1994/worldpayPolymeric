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
@TableName("email_template")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "邮件模板",description = "邮件模板")
public class EmailTemplateEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("template_number")
	@ApiModelProperty(name = "templateNumber",value = "模板编号",required = true,dataType = "String")
    private Integer templateNumber;
	
	@TableField("language")
	@ApiModelProperty(name = "language",value = "语言分类",required = true,dataType = "String")
    private String language;
	
	@TableField("language_name")
	@ApiModelProperty(name = "languageName",value = "语言分类",required = true,dataType = "String")
    private String languageName;
	
	@TableField("template_name")
	@ApiModelProperty(name = "templateName",value = "模板名称",required = true,dataType = "String")
    private String templateName;
	
	@TableField("template_subject")
	@ApiModelProperty(name = "templateSubject",value = "发送主题",required = true,dataType = "String")
    private String templateSubject;
	
	@TableField("template_content")
	@ApiModelProperty(name = "templateContent",value = "发送内容",required = true,dataType = "String")
    private String templateContent;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	
}
