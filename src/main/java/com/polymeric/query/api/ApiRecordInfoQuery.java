package com.polymeric.query.api;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApiRecordInfoQuery {

	@NotNull(message = "交易记录id不能为空")
    @ApiModelProperty(name = "id", value = "交易记录id", required = true, dataType = "Integer")
    private Integer id;
	
}
