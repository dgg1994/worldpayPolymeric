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
@TableName("merchants_user")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户用户信息",description = "商户用户信息")
public class MerchantsUserEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("mch_id")
	@ApiModelProperty(name = "mchId",value = "商户id",required = true,dataType = "String")
    private Integer mchId;
	
	@TableField("mch_appid")
	@ApiModelProperty(name = "mchAppid",value = "商户APPID",required = true,dataType = "String")
    private String mchAppid;
	
	@TableField("channel_id")
	@ApiModelProperty(name = "channelId",value = "渠道id",required = true,dataType = "String")
    private Integer channelId;
	
	@TableField("channel_code")
	@ApiModelProperty(name = "channelCode",value = "渠道编号",required = true,dataType = "String")
    private String channelCode;
	
	@TableField("api_uid")
	@ApiModelProperty(name = "apiUid",value = "三方uid",required = true,dataType = "String")
    private String apiUid;
	
	@TableField("user_email")
	@ApiModelProperty(name = "userEmail",value = "用户邮箱",required = true,dataType = "String")
    private String userEmail;
	
	@TableField("user_tel")
	@ApiModelProperty(name = "userTel",value = "用户手机号",required = true,dataType = "String")
    private String userTel;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
}
