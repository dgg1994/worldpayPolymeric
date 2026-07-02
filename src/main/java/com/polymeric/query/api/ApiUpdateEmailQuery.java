package com.polymeric.query.api;


import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiUpdateEmailQuery extends ApiBankCardIdQuery{
	
	@NotBlank(message = "邮箱地址不能为空")
    @ApiModelProperty(name = "email", value = "邮箱地址", required = true, dataType = "Integer")
	private String email;

}
