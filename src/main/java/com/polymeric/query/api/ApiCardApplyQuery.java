package com.polymeric.query.api;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 申请银行卡参数
 * @author Hlin
 *
 */
@Data
public class ApiCardApplyQuery {
	
	@NotNull(message = "产品id不能为空")
    @ApiModelProperty(name = "productId", value = "产品id,通过产品列表接口获得", required = true, dataType = "Integer")
    private Integer productId;
	
    @ApiModelProperty(name = "deliveryAddressId", value = "邮寄地址id，通过添加邮寄地址接口获得，申请实体卡时需传入", required = false, dataType = "Integer")
    private Integer deliveryAddressId;

}
