package com.polymeric.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.system.SysRoleMenuEntity;

@Repository
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity>{

	@Delete("delete from sys_role_menu where role_id = #{roleId}")
	void deleteRoleId(@Param("roleId") Integer roleId);

	@Select("select * from sys_role_menu where menu_id = #{menuId}")
	List<SysRoleMenuEntity> findMenuId(@Param("menuId") Integer menuId);


}
