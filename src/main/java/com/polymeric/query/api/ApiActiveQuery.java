package com.polymeric.query.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 激活银行卡参数
 * @author Hlin
 *
 */
@Data
public class ApiActiveQuery {
	
	@NotNull(message = "产品id不能为空")
    @ApiModelProperty(name = "productId", value = "产品id,通过产品列表接口获得", required = true, dataType = "Integer")
    private Integer productId;

	@NotBlank(message = "卡号不能为空")
    @ApiModelProperty(name = "cardNo", value = "卡号", required = true, dataType = "String")
    private String cardNo;
	
    @ApiModelProperty(name = "verifyCode", value = "激活码", required = false, dataType = "String")
    private String verifyCode;
    
	@NotBlank(message = "手机区号不能为空")
    @ApiModelProperty(name = "mobilePrefix", value = "手机区号", required = true, dataType = "String")
    private String mobilePrefix;
    
	@NotBlank(message = "手机号不能为空")
    @ApiModelProperty(name = "mobile", value = "手机号", required = true, dataType = "String")
    private String mobile;
    
	@NotBlank(message = "国家编码不能为空")
    @ApiModelProperty(name = "countryCode", value = "国家编码", required = true, dataType = "String")
    private String countryCode;
    
	@NotBlank(message = "地址1不能为空")
    @ApiModelProperty(name = "address", value = "地址1", required = true, dataType = "String")
    private String address;
	
    @ApiModelProperty(name = "address2", value = "地址2", required = true, dataType = "String")
    private String address2;
	
	@NotBlank(message = "城市不能为空")
    @ApiModelProperty(name = "city", value = "城市", required = true, dataType = "String")
    private String city;
	
	@NotBlank(message = "省/州不能为空")
    @ApiModelProperty(name = "state", value = "省/州", required = true, dataType = "String")
    private String state;
	
	@NotBlank(message = "邮编不能为空")
    @ApiModelProperty(name = "postCode", value = "邮编", required = true, dataType = "String")
    private String postCode;
	
	
}
