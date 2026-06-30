package com.polymeric.entity.merchants;

import java.util.Date;
import java.util.List;

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
@TableName("merchants_ip")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户IP白名单",description = "商户IP白名单")
public class MerchantsIpEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("merchants_id")
	@ApiModelProperty(name = "merchantsId",value = "商户id",required = true,dataType = "Integer")
    private Integer merchantsId;
	
	@TableField("app_id")
	@ApiModelProperty(name = "appId",value = "APPID",required = true,dataType = "String")
    private String appId;
	
	@TableField("ip_address")
	@ApiModelProperty(name = "ipAddress",value = "IP地址",required = true,dataType = "String")
    private String ipAddress;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "ipWhitelist",value = "IP地址多个",required = false,dataType = "String")
	private List<String> ipWhitelist;
	

	
}
