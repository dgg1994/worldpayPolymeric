package com.polymeric.query.pub;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公共查询条件",description = "公共查询条件")
public class PublicQueryEntity extends PageQueryHelperEntity{
	
	@TableField(exist = false)
	@ApiModelProperty(name = "begTime",value = "查询条件开始时间",required = false,dataType = "String")
    private String begTime;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "endTime",value = "查询条件结束时间",required = false,dataType = "String")
    private String endTime;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "status",value = "状态",required = false,dataType = "String")
    private String status;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "statusId",value = "状态",required = false,dataType = "String")
    private Integer statusId;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "type",value = "分类",required = false,dataType = "Integer")
    private String type;

	@TableField(exist = false)
	@ApiModelProperty(name = "userId",value = "渠道商用户id",required = false,dataType = "String")
	private String userId;

	@TableField(exist = false)
	@ApiModelProperty(name = "uid",value = "uid",required = false,dataType = "List")
	private List<String> uid;
	
	

}
