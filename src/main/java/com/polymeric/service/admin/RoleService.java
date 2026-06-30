package com.polymeric.service.admin;
import com.polymeric.base.ResponseBase;
import com.polymeric.entity.system.SysRoleEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 *  @category 角色接口
 */
@RequestMapping("/role")
@Api(value = "角色接口",tags = "角色接口")
public interface RoleService {

	@PostMapping("/findAll")
	@ApiOperation(value = "查询所有角色",notes = "查询所有角色",response = ResponseBase.class)
	ResponseBase findAll(SysRoleEntity entity);

	@GetMapping("/findById")
	@ApiOperation(value = "查询角色",notes = "查询角色",response = ResponseBase.class)
	ResponseBase findById(Integer roleId);
	
	@PostMapping("/add")
	@ApiOperation(value = "新增角色",notes = "新增角色",response = ResponseBase.class)
	ResponseBase add(SysRoleEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑角色信息",notes = "编辑角色信息",response = ResponseBase.class)
	ResponseBase update(SysRoleEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除角色",notes = "编辑角色信息",response = ResponseBase.class)
	ResponseBase delete(Integer roleId);
	
	@PostMapping("/changeStatus")
	@ApiOperation(value = "角色停用启用",notes = "角色停用启用",response = ResponseBase.class)
	ResponseBase changeStatus(Integer roleId,String status);
	
	@GetMapping("/findNormalRole")
	@ApiOperation(value = "查询正常状态下角色信息",notes = "查询正常状态下角色信息",response = ResponseBase.class)
	ResponseBase findNormalRole();
	
	
}
