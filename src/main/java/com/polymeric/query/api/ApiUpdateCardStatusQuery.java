package com.polymeric.query.api;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiUpdateCardStatusQuery extends ApiBankCardIdQuery{
	
	@NotNull(message = "状态不能为空")
	@ApiModelProperty(name = "enable", value = "状态", required = true, dataType = "Boolean")
	private Boolean enable;
	

}
