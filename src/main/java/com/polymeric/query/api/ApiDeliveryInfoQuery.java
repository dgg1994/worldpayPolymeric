package com.polymeric.query.api;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 查询邮寄信息参数
 * @author Hlin
 *
 */
@Data
public class ApiDeliveryInfoQuery {

	@NotBlank(message = "订单号不能为空")
    @ApiModelProperty(name = "orderNo", value = "订单号", required = true, dataType = "String")
    private String orderNo;
	
}
