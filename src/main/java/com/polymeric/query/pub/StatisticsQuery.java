package com.polymeric.query.pub;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticsQuery {

	@ApiModelProperty(name = "sysAccountId",value = "登录用户id",required = false,dataType = "Integer")
    private Integer sysAccountId;
	
	@ApiModelProperty(name = "mchId",value = "商户id",required = false,dataType = "Integer")
    private Integer mchId;
	
	@ApiModelProperty(name = "mchAppid",value = "商户AppID",required = false,dataType = "String")
    private String mchAppid;
	
	@ApiModelProperty(name = "oderStatus",value = "订单状态",required = false,dataType = "Integer")
    private Integer oderStatus;
	
	@ApiModelProperty(name = "oderType",value = "订单类型",required = false,dataType = "Integer")
    private Integer oderType;
	
	@ApiModelProperty(name = "startTime",value = "开始时间",required = false,dataType = "String")
    private String startTime;
	
	@ApiModelProperty(name = "endTime",value = "结束时间",required = false,dataType = "String")
    private String endTime;
	
}
