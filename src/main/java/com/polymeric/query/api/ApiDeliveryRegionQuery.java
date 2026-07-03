package com.polymeric.query.api;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 用于查询邮寄地区列表参数
 * @author Hlin
 *
 */
@Data
public class ApiDeliveryRegionQuery {
	
	@NotBlank(message = "地区不能为空")
    @ApiModelProperty(name = "local", value = "地区", required = true, dataType = "Integer")
    private String local;

}
