package com.polymeric.query.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiRecordQuery extends ApiBankCardIdQuery{
	
    @ApiModelProperty(name = "pageNum", value = "页码，默认第一页", required = true, dataType = "Integer")
    private Integer pageNum;
    
    @ApiModelProperty(name = "pageSize", value = "每页数量，默认10条", required = true, dataType = "Integer")
    private Integer pageSize;
    
    @ApiModelProperty(name = "startTime", value = "开始时间，时间戳毫秒数", required = true, dataType = "Long")
    private Long startTime;
    
    @ApiModelProperty(name = "endTime", value = "结束时间，时间戳毫秒数", required = true, dataType = "Long")
    private Long endTime;

}
