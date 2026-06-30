package com.polymeric.service.admin;
import com.polymeric.base.ResponseBase;
import com.polymeric.entity.system.SysMenuEntity;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @category 菜单管理接口
 *
 */
@RequestMapping("/menu")
//@Api(value = "菜单管理接口",tags = "菜单管理接口")
public interface MenuService {
	
	@PostMapping("/findByUser")
	@ApiOperation(value = "获取用户菜单",notes = "获取用户菜单",response = ResponseBase.class)
	ResponseBase findByUser(Integer userId);
	
    @PostMapping("/findAll")
    @ApiOperation(value = "获取所有菜单列表",notes = "获取所有菜单菜单列表",response = ResponseBase.class)
    ResponseBase findAll(SysMenuEntity entity);
    
    @PostMapping("/treeselect")
	@ApiOperation(value = "获取树结构菜单",notes = "获取树结构菜单",response = ResponseBase.class)
	ResponseBase treeselect(SysMenuEntity entity);
    
    @GetMapping("/roleMenuTreeselect")
	@ApiOperation(value = "获取对应角色树结构菜单",notes = "获取对应角色树结构菜单",response = ResponseBase.class)
	ResponseBase roleMenuTreeselect(Integer roleId);
    
    @PostMapping("/add")
	@ApiOperation(value = "新增菜单",notes = "新增菜单",response = ResponseBase.class)
	ResponseBase add(SysMenuEntity entity);
    
    @GetMapping("/findById")
	@ApiOperation(value = "获取菜单详情",notes = "获取菜单详情",response = ResponseBase.class)
	ResponseBase findById(Integer menuId);
    
    @PostMapping("/update")
	@ApiOperation(value = "获取菜单详情",notes = "获取菜单详情",response = ResponseBase.class)
	ResponseBase update(SysMenuEntity entity);
    
    @GetMapping("/delete")
	@ApiOperation(value = "获取菜单详情",notes = "获取菜单详情",response = ResponseBase.class)
	ResponseBase delete(Integer menuId);
	
}
