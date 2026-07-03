package com.polymeric.query.api;

import com.polymeric.constants.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiRecordQuery extends ApiBankCardIdQuery{
	
	@ApiModelProperty(name = "pageNumber",value = "分页页码",required = false,dataType = "Integer")
	private Integer pageNumber = Constants.PAGENUMBER;
	
	@ApiModelProperty(name = "pageSize",value = "分页数量",required = false,dataType = "Integer")
	private Integer pageSize =Constants.PAGESIZE;
    

}
