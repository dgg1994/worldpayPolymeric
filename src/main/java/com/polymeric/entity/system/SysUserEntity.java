package com.polymeric.entity.system;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.polymeric.query.pub.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@ApiModel(value = "用户",description = "用户")
public class SysUserEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private Integer id;
	
	@TableField("username")
	@ApiModelProperty(name = "username",value = "用户名",required = true,dataType = "String")
    private String username;

	@TableField("tel")
	@ApiModelProperty(name = "tel",value = "手机号",required = true,dataType = "String")
    private String tel;
    
	@TableField("acctive")
	@ApiModelProperty(name = "acctive",value = "账号",required = true,dataType = "String")
    private String acctive;
    
	@TableField("head_pic")
	@ApiModelProperty(name = "headPic",value = "头像",required = true,dataType = "String")
    private String headPic;
    
	@TableField("user_state")
	@ApiModelProperty(name = "userState",value = "用户状态，2：离职，1：正常，0：账号注销",required = true,dataType = "Integer")
    private Integer userState;
	
	@TableField("password")
	@ApiModelProperty(name = "password",value = "密码",required = true,dataType = "String")
    private String password;
	
	@TableField("create_user")
	@ApiModelProperty(name = "createUser",value = "创建人",required = true,dataType = "Integer")
    private Integer createUser;
	
	@TableField("create_time")
	@ApiModelProperty(name = "createTime",value = "创建时间",required = true,dataType = "String")
    private String createTime;
	
	@TableField("login_time")
	@ApiModelProperty(name = "loginTime",value = "登录时间",required = true,dataType = "String")
    private Date loginTime;
	
	@TableField("google_secretkey")
	@ApiModelProperty(name = "googleSecretkey",value = "谷歌密钥",required = true,dataType = "String")
    private String googleSecretkey;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "userStateNmae",value = "用户状态名称",required = false,dataType = "Integer")
    private String userStateNmae;
    
	@TableField(exist = false)
	@ApiModelProperty(name = "roleId",value = "角色集合",required = true,dataType = "List<Integer>")
    private List<Integer> roleId;
    
	@TableField(exist = false)
	@ApiModelProperty(name = "power",value = "权限集合",required = true,dataType = "Collection<GrantedAuthority>")
    private List<SysUserPowerEntity> power;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "roleList",value = "角色集合",required = true,dataType = "List<Map<String, Object>>")
    private List<SysRoleEntity> roleList;
    
	@TableField(exist = false)
	@ApiModelProperty(name = "roles",value = "角色标识",required = true,dataType = "List")
    private List<String>  roles;

	@TableField(exist = false)
	@ApiModelProperty(name = "permissions",value = "权限标识",required = true,dataType = "List")
    private List<String>  permissions;

	@TableField(exist = false)
	private Integer signCount;
	
	@TableField(exist = false)
	private Integer adminRole;
	
	@TableField(exist = false)
	private Integer deleteState;
	
	@TableField(exist = false)
	private Integer deptId;
	
	@TableField(exist = false)
	private Integer manageId;
	
	@TableField(exist = false)
	private Integer googleCode;
	

	public SysUserEntity() {
		super();
	}


}

