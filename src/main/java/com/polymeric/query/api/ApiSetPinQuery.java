package com.polymeric.query.api;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiSetPinQuery extends ApiBankCardIdQuery{
		
	@NotNull(message = "pin不能为空")
    @ApiModelProperty(name = "pin", value = "pin", required = true, dataType = "Integer")
    private String pin;

}
