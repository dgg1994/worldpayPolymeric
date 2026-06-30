package com.polymeric.entity.merchants;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("merchants_key")
@ApiModel(value = "商户安全配置",description = "商户安全配置")
public class MerchantsKeyEntity {
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("merchants_id")
	@ApiModelProperty(name = "merchantsId",value = "商户id",required = true,dataType = "Integer")
    private Integer merchantsId;
	
	@TableField("app_id")
	@ApiModelProperty(name = "appId",value = "APPID",required = true,dataType = "String")
    private String appId;
	
	@TableField("private_key")
	@ApiModelProperty(name = "privateKey",value = "私钥",required = true,dataType = "String")
    private String privateKey;
	
	@TableField("public_key")
	@ApiModelProperty(name = "publicKey",value = "公钥",required = true,dataType = "String")
    private String publicKey;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	
}
