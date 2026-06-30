package com.polymeric.dao.system;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.system.SysRoleEntity;

@Repository
public interface SysRoleDao extends BaseMapper<SysRoleEntity>{

	@Select("select * from sys_role where role_name = #{roleName} and del_flag = #{delFlag}")
	SysRoleEntity findName(@Param("roleName") String roleName, @Param("delFlag") String delFlag);

	@Select("select * from sys_role where role_id != #{roleId} and role_name = #{roleName} and del_flag = #{delFlag}")
	SysRoleEntity findRoleIdName(@Param("roleId") Integer roleId,@Param("roleName") String roleName, @Param("delFlag") String delFlag);

	@Select("select * from sys_role where status = #{status} and del_flag = #{delFlag}")
	List<SysRoleEntity> findNormalRole(@Param("delFlag")String delFlag,@Param("status") String status,@Param("roleId") Integer id);

	@Select("<script>"
			+ "select * from sys_role where del_flag = #{delFlag} "
			+ "<if test = 'roleName != null'> and role_name like '%${roleName}%'</if>"
			+ "<if test = 'roleKey != null'> and role_key like '%${roleKey}%'</if>"
			+ "<if test = 'status != null'> and status =#{status}</if>"
			+ " order by role_sort"
			+ "</script>")
	List<SysRoleEntity> findList(SysRoleEntity entity);


	@Select("select * from sys_role where role_key = #{roleKey}")
    SysRoleEntity findRoleIdByRoleKey(@Param("roleKey") String roleKey);
}
