package com.polymeric.entity.system;

import java.util.Date;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色表 sys_role
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@ApiModel(value = "角色对象", description = "角色对象")
public class SysRoleEntity extends BaseEntity  {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "roleId", value = "角色id", required = true, dataType = "Integer")
	private Integer roleId;

	@TableField("role_name")
	@ApiModelProperty(name = "roleName", value = "角色名称", required = true, dataType = "String")
	private String roleName;

	@TableField("role_key")
	@ApiModelProperty(name = "roleKey", value = "角色权限", required = true, dataType = "String")
	private String roleKey;

	@TableField("role_sort")
	@ApiModelProperty(name = "roleSort", value = "角色排序", required = true, dataType = "Integer")
	private Integer roleSort;

	@TableField("data_scope")
	@ApiModelProperty(name = "dataScope", value = "数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）", required = true, dataType = "String")
	private String dataScope;

	@TableField("menu_check_strictly")
	@ApiModelProperty(name = "menuCheckStrictly", value = " 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）", required = true, dataType = "boolean")
	private boolean menuCheckStrictly;

	@TableField("dept_check_strictly")
	@ApiModelProperty(name = "deptCheckStrictly", value = "部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）", required = true, dataType = "boolean")
	private boolean deptCheckStrictly;

	@TableField("status")
	@ApiModelProperty(name = "status", value = " 角色状态（0正常 1停用）", required = true, dataType = "String")
	private String status;

	@TableField("del_flag")
	@ApiModelProperty(name = "delFlag", value = "删除标志（0代表存在 2代表删除）", required = true, dataType = "String")
	private String delFlag;

	@TableField("create_by")
	@ApiModelProperty(name = "createBy", value = "创建者", required = true, dataType = "String")
	private String createBy;

	@TableField("create_time")
	@ApiModelProperty(name = "createTime", value = "创建时间", required = true, dataType = "Date")
	private Date createTime;

	@TableField("update_by")
	@ApiModelProperty(name = "updateBy", value = "更新者", required = true, dataType = "String")
	private String updateBy;

	@TableField("update_time")
	@ApiModelProperty(name = "updateTime", value = "更新时间", required = true, dataType = "Date")
	private Date updateTime;
	
	@TableField("remark")
	@ApiModelProperty(name = "remark", value = "备注", required = true, dataType = "String")
	private String remark;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "menuIds", value = "菜单组 ", required = false, dataType = "Long[]")
	private Integer pageNum;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "menuIds", value = "菜单组 ", required = false, dataType = "Long[]")
	private Integer pageSize;

	@TableField(exist = false)
	@ApiModelProperty(name = "flag", value = "用户是否存在此角色标识 默认不存在", required = false, dataType = "boolean")
	private boolean flag = false;

	@TableField(exist = false)
	@ApiModelProperty(name = "menuIds", value = "菜单组 ", required = false, dataType = "Long[]")
	private Integer[] menuIds;

	@TableField(exist = false)
	@ApiModelProperty(name = "deptIds", value = "部门组（数据权限）", required = false, dataType = "Long[]")
	private Long[] deptIds;

	@TableField(exist = false)
	@ApiModelProperty(name = "permissions", value = "角色菜单权限", required = false, dataType = "Set<String>")
	private Set<String> permissions;
	

	public SysRoleEntity() {

	}

	public SysRoleEntity(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public boolean isAdmin() {
		return isAdmin(this.roleId);
	}

	public static boolean isAdmin(Integer roleId) {
		return roleId != null && 1L == roleId;
	}

	@NotBlank(message = "角色名称不能为空")
	@Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@NotBlank(message = "权限字符不能为空")
	@Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	@NotNull(message = "显示顺序不能为空")
	public Integer getRoleSort() {
		return roleSort;
	}

	public void setRoleSort(Integer roleSort) {
		this.roleSort = roleSort;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public boolean isMenuCheckStrictly() {
		return menuCheckStrictly;
	}

	public void setMenuCheckStrictly(boolean menuCheckStrictly) {
		this.menuCheckStrictly = menuCheckStrictly;
	}

	public boolean isDeptCheckStrictly() {
		return deptCheckStrictly;
	}

	public void setDeptCheckStrictly(boolean deptCheckStrictly) {
		this.deptCheckStrictly = deptCheckStrictly;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Integer[] getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(Integer[] menuIds) {
		this.menuIds = menuIds;
	}

	public Long[] getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(Long[] deptIds) {
		this.deptIds = deptIds;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("roleId", getRoleId())
				.append("roleName", getRoleName()).append("roleKey", getRoleKey()).append("roleSort", getRoleSort())
				.append("dataScope", getDataScope()).append("menuCheckStrictly", isMenuCheckStrictly())
				.append("deptCheckStrictly", isDeptCheckStrictly()).append("status", getStatus())
				.append("delFlag", getDelFlag()).append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.toString();
	}
}
