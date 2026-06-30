package com.polymeric.entity.system;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_user_role")
@ApiModel(value = "用户角色",description = "用户角色")
public class SysUserRoleEntity {

	@TableId(type = IdType.AUTO)
    @ApiModelProperty(name = "",value = "",required = true,dataType = "")
    private Integer id;

    @TableField("user_id")
    @ApiModelProperty(name = "userId",value = "用户id",required = true,dataType = "Integer")
    private Integer userId;
    
    @TableField("role_id")
    @ApiModelProperty(name = "roleId",value = "角色id",required = true,dataType = "Integer")
    private Integer roleId;

    @TableField("create_user")
    @ApiModelProperty(name = "createUser",value = "创建人",required = true,dataType = "Integer")
    private Integer createUser;
    
    @TableField("create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间",required = true,dataType = "")
    private Date createTime;
	
}
