package com.polymeric.query.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 添加邮寄地址参数
 * @author Hlin
 *
 */
@Data
public class ApiAddressAddQuery {
	
	@NotNull(message = "邮寄地区Id不能为空")
    @ApiModelProperty(name = "countryRegionId", value = "邮寄地区Id，查询邮寄地区列表接口获得", required = true, dataType = "Integer")
    private Integer countryRegionId;

	@NotBlank(message = "国家不能为空")
    @ApiModelProperty(name = "country", value = "国家", required = true, dataType = "String")
    private String country;
	
	@NotBlank(message = "城市不能为空")
    @ApiModelProperty(name = "city", value = "城市", required = true, dataType = "Integer")
    private String city;
	
	@NotBlank(message = "接受人不能为空")
    @ApiModelProperty(name = "receiverName", value = "接受人", required = true, dataType = "Integer")
    private String receiverName;
	
	@NotBlank(message = "接受人电话不能为空")
    @ApiModelProperty(name = "receiverMobile", value = "接受人电话", required = true, dataType = "Integer")
    private String receiverMobile;
	
	@NotBlank(message = "邮寄地址不能为空")
    @ApiModelProperty(name = "receiverAddress", value = "邮寄地址", required = true, dataType = "Integer")
    private String receiverAddress;
	
	@NotBlank(message = "邮编不能为空")
    @ApiModelProperty(name = "postCode", value = "邮编", required = true, dataType = "Integer")
    private String postCode;
	
}
