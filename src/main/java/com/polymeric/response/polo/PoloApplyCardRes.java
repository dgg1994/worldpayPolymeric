package com.polymeric.response.polo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PoloApplyCardRes {

	@ApiModelProperty(name = "userBankcardId",value = "银行卡唯一id",required = true,dataType = "String")
	private Integer userBankcardId;
	
	@ApiModelProperty(name = "cardNo",value = "银行卡号",required = true,dataType = "String")
	private String cardNo;
	
	@ApiModelProperty(name = "orderNo",value = "订单号",required = true,dataType = "String")
	private String orderNo;
	
}
