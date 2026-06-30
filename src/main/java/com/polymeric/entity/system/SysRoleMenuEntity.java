package com.polymeric.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 角色菜单关联
 */
@Data
@TableName("sys_role_menu")
@ApiModel(value = "角色菜单关联对象",description = "角色菜单关联对象")
public class SysRoleMenuEntity {
	
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
    
    @TableField("role_id")
    @ApiModelProperty(name = "roleId",value = "角色id",required = true,dataType = "Integer")
    private Integer roleId;
    
    @TableField("menu_id")
    @ApiModelProperty(name = "menuId",value = "菜单id",required = true,dataType = "Integer")
    private Integer menuId;
    
    @TableField("create_user")
    @ApiModelProperty(name = "createUser",value = "创建人",required = true,dataType = "Integer")
    private Integer createUser;
    
    @TableField("create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间",required = false,dataType = "Date")
    private Date createTime;
}
