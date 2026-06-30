package com.polymeric.pubapi.query.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * KYC申请请求参数
 * 
 * @author 
 * @category KYC申请
 */
@Data
public class ApiKycApplyQuery {

    @ApiModelProperty(name = "firstName", value = "英文名", required = true, dataType = "String")
    @NotBlank(message = "英文名不能为空")
    private String firstName;

    @ApiModelProperty(name = "lastName", value = "英文姓", required = true, dataType = "String")
    @NotBlank(message = "英文姓不能为空")
    private String lastName;

    @ApiModelProperty(name = "idNo", value = "证件号", required = true, dataType = "String")
    @NotBlank(message = "证件号不能为空")
    private String idNo;

    @ApiModelProperty(name = "email", value = "电子邮件", required = true, dataType = "String")
    @NotBlank(message = "电子邮件不能为空")
    private String email;

    @ApiModelProperty(name = "nationCode", value = "国籍，ISO 3166-1 Alpha-3 代码，例如SGP", required = true, dataType = "String")
    @NotBlank(message = "国籍不能为空")
    private String nationCode;

    @ApiModelProperty(name = "certType", value = "证件类型：1身份证（需正面+背面照）、2护照（仅需正面照）、3驾照（需正面+背面照）", required = true, dataType = "Integer", allowableValues = "1,2,3")
    @NotNull(message = "证件类型不能为空")
    private Integer certType;

    @ApiModelProperty(name = "idUrl", value = "证件照正面url", required = true, dataType = "String")
    @NotBlank(message = "证件照正面url不能为空")
    private String idUrl;

    @ApiModelProperty(name = "idBackUrl", value = "证件照反面url", required = false, dataType = "String")
    private String idBackUrl;

    @ApiModelProperty(name = "birthday", value = "生日", required = true, dataType = "String", example = "1990-01-01")
    @NotBlank(message = "生日不能为空")
    private String birthday;

    @ApiModelProperty(name = "countryCode", value = "居住国家，ISO 3166-1 Alpha-3 代码，例如SGP", required = true, dataType = "String")
    @NotBlank(message = "居住国家不能为空")
    private String countryCode;

    @ApiModelProperty(name = "areaCode", value = "手机区号，例如86", required = true, dataType = "String")
    @NotBlank(message = "手机区号不能为空")
    private String areaCode;

    @ApiModelProperty(name = "phone", value = "手机号", required = true, dataType = "String")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(name = "fileType", value = "文件类型，国家列表中返回需要补充资料时才传该字段。枚举值: 7-电子签证, 8-签证照片, 9-永久居留卡（PR）, 10-临时居留证（TRC）, 11-就业许可卡, 12-学生居留卡, 13-20", required = false, dataType = "Integer", allowableValues = "7,8,9,10,11,12,13,14,15,16,17,18,19,20")
    private Integer fileType;

    @ApiModelProperty(name = "fileUrl", value = "文件url，国家列表中返回需要补充资料时才传该字段", required = false, dataType = "String")
    private String fileUrl;

    @ApiModelProperty(name = "faceUrl", value = "人脸报告url，国家列表中返回需要补充资料时才传该字段", required = false, dataType = "String")
    private String faceUrl;

    @ApiModelProperty(name = "referenceId", value = "获取人脸报告第三方的sessionId", required = false, dataType = "String")
    private String referenceId;

    @ApiModelProperty(name = "referenceType", value = "第三方报告类型，例如 SUMSUB", required = false, dataType = "String")
    private String referenceType;

    @ApiModelProperty(name = "selfieUrl", value = "人脸自拍照url", required = false, dataType = "String")
    private String selfieUrl;
}