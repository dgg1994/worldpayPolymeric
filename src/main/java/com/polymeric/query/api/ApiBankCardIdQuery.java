package com.polymeric.query.api;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApiBankCardIdQuery {

	@NotNull(message = "银行卡id不能为空")
    @ApiModelProperty(name = "userBankcardId", value = "银行卡id", required = true, dataType = "Integer")
    private Integer userBankcardId;
	
}
