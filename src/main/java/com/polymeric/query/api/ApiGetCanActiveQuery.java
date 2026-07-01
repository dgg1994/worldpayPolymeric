package com.polymeric.query.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 银行卡是否可激活参数
 * @author Hlin
 *
 */
@Data
public class ApiGetCanActiveQuery {
	
	@NotBlank(message = "卡号不能为空")
    @ApiModelProperty(name = "cardNo", value = "卡号", required = true, dataType = "Integer")
    private String cardNo;
	
	@NotNull(message = "激活码不能为空")
    @ApiModelProperty(name = "verifyCode", value = "激活码", required = true, dataType = "Integer")
    private String verifyCode;

}
