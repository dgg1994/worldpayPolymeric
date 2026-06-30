package com.polymeric.entity.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * 菜单权限表 sys_menu
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
@ApiModel(value = "菜单权限", description = "菜单权限")
public class SysMenuEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "menuId", value = "菜单ID", required = true, dataType = "Integer")
	private Integer menuId;

	@TableField("menu_name")
	@ApiModelProperty(name = "menuName", value = "菜单名称", required = true, dataType = "Integer")
	private String menuName;

	@TableField(exist = false)
	@ApiModelProperty(name = "parentName", value = "父菜单名称", required = true, dataType = "String")
	private String parentName;

	@TableField("parent_id")
	@ApiModelProperty(name = "parentId", value = "父菜单ID", required = true, dataType = "Integer")
	private Integer parentId;

	@TableField("order_num")
	@ApiModelProperty(name = "orderNum", value = "显示顺序", required = true, dataType = "Integer")
	private Integer orderNum;

	@TableField("path")
	@ApiModelProperty(name = "path", value = "路由地址", required = true, dataType = "String")
	private String path;

	@TableField("component")
	@ApiModelProperty(name = "component", value = "组件路径", required = true, dataType = "String")
	private String component;

	@TableField("query")
	@ApiModelProperty(name = "query", value = " 路由参数", required = true, dataType = "String")
	private String query;

	@TableField("is_frame")
	@ApiModelProperty(name = "isFrame", value = "是否为外链（0是 1否）", required = true, dataType = "String")
	private String isFrame;

	@TableField("is_cache")
	@ApiModelProperty(name = "isCache", value = "是否缓存（0缓存 1不缓存）", required = true, dataType = "String")
	private String isCache;

	@TableField("menu_type")
	@ApiModelProperty(name = "menuType", value = "类型（M目录 C菜单 F按钮）", required = true, dataType = "String")
	private String menuType;

	@TableField("visible")
	@ApiModelProperty(name = "visible", value = "显示状态（0显示 1隐藏）", required = true, dataType = "String")
	private String visible;

	@TableField("status")
	@ApiModelProperty(name = "status", value = "菜单状态（0正常 1停用）", required = true, dataType = "String")
	private String status;

	@TableField("perms")
	@ApiModelProperty(name = "perms", value = " 权限字符串 ", required = true, dataType = "String")
	private String perms;

	@TableField("icon")
	@ApiModelProperty(name = "icon", value = "菜单图标", required = true, dataType = "String")
	private String icon;

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
	@ApiModelProperty(name = "children", value = "子菜单", required = true, dataType = "List")
	private List<SysMenuEntity> children = new ArrayList<SysMenuEntity>();

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	@NotBlank(message = "菜单名称不能为空")
	@Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@NotNull(message = "显示顺序不能为空")
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	@Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getIsFrame() {
		return isFrame;
	}

	public void setIsFrame(String isFrame) {
		this.isFrame = isFrame;
	}

	public String getIsCache() {
		return isCache;
	}

	public void setIsCache(String isCache) {
		this.isCache = isCache;
	}

	@NotBlank(message = "菜单类型不能为空")
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<SysMenuEntity> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenuEntity> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("menuId", getMenuId())
				.append("menuName", getMenuName()).append("parentId", getParentId()).append("orderNum", getOrderNum())
				.append("path", getPath()).append("component", getComponent()).append("isFrame", getIsFrame())
				.append("IsCache", getIsCache()).append("menuType", getMenuType()).append("visible", getVisible())
				.append("status ", getStatus()).append("perms", getPerms()).append("icon", getIcon())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.toString();
	}
}
