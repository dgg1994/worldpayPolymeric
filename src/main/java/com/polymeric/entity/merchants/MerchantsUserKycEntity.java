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
@TableName("merchants_user_kyc")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户用户kyc信息", description = "商户用户kyc信息")
public class MerchantsUserKycEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "主键", required = true, dataType = "Integer")
	private Integer id;
	
	@TableField("user_id")
	@ApiModelProperty(name = "userId", value = "用户id", required = true, dataType = "String")
	private Integer userId;
	
	@TableField("user_uid")
	@ApiModelProperty(name = "userUid", value = "用户uid", required = true, dataType = "String")
	private String userUid;

	@TableField("mch_id")
	@ApiModelProperty(name = "mchId", value = "商户id", required = true, dataType = "String")
	private Integer mchId;

	@TableField("mch_appid")
	@ApiModelProperty(name = "mchAppid", value = "商户APPID", required = true, dataType = "String")
	private String mchAppid;

	@TableField("first_name")
	@ApiModelProperty(name = "firstName", value = "英文名", required = true, dataType = "String")
	private String firstName;

	@TableField("last_name")
	@ApiModelProperty(name = "lastName", value = "英文姓", required = true, dataType = "String")
	private String lastName;

	@TableField("id_no")
	@ApiModelProperty(name = "idNo", value = "证件号", required = true, dataType = "String")
	private String idNo;

	@TableField("user_email")
	@ApiModelProperty(name = "userEmail", value = "电子邮件", required = true, dataType = "String")
	private String userEmail;

	@TableField("nation_code")
	@ApiModelProperty(name = "nationCode", value = "国籍，ISO 3166-1 Alpha-3 代码，例如SGP", required = true, dataType = "String")
	private String nationCode;

	@TableField("cert_type")
	@ApiModelProperty(name = "certType", value = "证件类型：1身份证（需正面+背面照）、2护照（仅需正面照）、3驾照（需正面+背面照）", required = true, dataType = "Integer", allowableValues = "1,2,3")
	private Integer certType;

	@TableField("id_url")
	@ApiModelProperty(name = "idUrl", value = "证件照正面url", required = true, dataType = "String")
	private String idUrl;

	@TableField("id_back_url")
	@ApiModelProperty(name = "idBackUrl", value = "证件照反面url", required = false, dataType = "String")
	private String idBackUrl;

	@TableField("user_birthday")
	@ApiModelProperty(name = "userBirthday", value = "生日", required = true, dataType = "String", example = "1990-01-01")
	private String userBirthday;

	@TableField("country_code")
	@ApiModelProperty(name = "countryCode", value = "居住国家，ISO 3166-1 Alpha-3 代码，例如SGP", required = true, dataType = "String")
	private String countryCode;

	@TableField("area_code")
	@ApiModelProperty(name = "areaCode", value = "手机区号，例如86", required = true, dataType = "String")
	private String areaCode;

	@TableField("user_phone")
	@ApiModelProperty(name = "userPhone", value = "手机号", required = true, dataType = "String")
	private String userPhone;

	@TableField("file_type")
	@ApiModelProperty(name = "fileType", value = "文件类型，国家列表中返回需要补充资料时才传该字段。枚举值: 7-电子签证, 8-签证照片, 9-永久居留卡（PR）, 10-临时居留证（TRC）, 11-就业许可卡, 12-学生居留卡, 13-20", required = false, dataType = "Integer", allowableValues = "7,8,9,10,11,12,13,14,15,16,17,18,19,20")
	private Integer fileType;

	@TableField("file_url")
	@ApiModelProperty(name = "fileUrl", value = "文件url，国家列表中返回需要补充资料时才传该字段", required = false, dataType = "String")
	private String fileUrl;

	@TableField("face_url")
	@ApiModelProperty(name = "faceUrl", value = "人脸报告url，国家列表中返回需要补充资料时才传该字段", required = false, dataType = "String")
	private String faceUrl;

	@TableField("reference_id")
	@ApiModelProperty(name = "referenceId", value = "获取人脸报告第三方的sessionId", required = false, dataType = "String")
	private String referenceId;

	@TableField("reference_type")
	@ApiModelProperty(name = "referenceType", value = "第三方报告类型，例如 SUMSUB", required = false, dataType = "String")
	private String referenceType;

	@TableField("selfie_url")
	@ApiModelProperty(name = "selfieUrl", value = "人脸自拍照url", required = false, dataType = "String")
	private String selfieUrl;

	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmtModified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
}
