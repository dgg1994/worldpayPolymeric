package com.polymeric.query.api;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 3ds授权通过参数
 * @author Hlin
 *
 */
@Data
public class ApiAuthApproveQuery {

	@NotBlank(message = "认证id不能为空")
    @ApiModelProperty(name = "authId", value = "认证id,webhook通知获取", required = true, dataType = "Integer")
    private String authId;
	
}
