package com.polymeric.entity.system;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

/**
 * Entity基类
 * 
 * @author ruoyi
 */
public class BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @TableField(exist = false)
	@ApiModelProperty(name = "searchValue", value = "搜索值", required = false, dataType = "String")
    private String searchValue;

    @TableField(exist = false)
	@ApiModelProperty(name = "createBy", value = "创建者", required = false, dataType = "String")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
	@ApiModelProperty(name = "createTime", value = "创建时间", required = false, dataType = "Date")
    private Date createTime;

    @TableField(exist = false)
	@ApiModelProperty(name = "updateBy", value = "更新者", required = false, dataType = "String")
    private String updateBy;

    @TableField(exist = false)
	@ApiModelProperty(name = "updateTime", value = "更新时间", required = false, dataType = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
   	@ApiModelProperty(name = "remark", value = " 备注", required = false, dataType = "String")
    private String remark;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
   	@ApiModelProperty(name = "params", value = " 请求参数", required = false, dataType = "Map")
    private Map<String, Object> params;

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
}
