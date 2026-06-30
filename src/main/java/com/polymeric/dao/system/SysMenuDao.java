package com.polymeric.dao.system;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.system.SysMenuEntity;

@Repository
public interface SysMenuDao extends BaseMapper<SysMenuEntity>{

	@Select("<script>" +
			"select * from sys_menu where 1 = 1 " +
			"<if test = 'menuName != null'> and menu_name like '%${menuName}%' </if>" +
			"<if test = 'menuType != null'> and menu_type = #{menuType}' </if>" +
			"<if test = 'status != null'> and status = #{status} </if>" +
			" order by order_num"+
			"</script>")
	List<SysMenuEntity> findAll(SysMenuEntity entity);
	
	@Select("select * from sys_menu where parent_id = #{parentId} and menu_id in "
            + "(select menu_id from sys_role_menu where role_id in "
            + "(select role_id from sys_user_role where user_id = #{userId})) order by order_num")
	List<SysMenuEntity> findUserParentMenu(@Param("userId") Integer userId,@Param("parentId") Integer parentId);

	@Select("select * from sys_menu where parent_id = #{parentId} order by order_num")
	List<SysMenuEntity> findByParentId(@Param("parentId")Integer parentId);

	@Select("<script>"
			+ "select *,ifnull(perms,'') as perms from sys_menu where 1=1 "
			+ "<if test ='menuName != null'> and menu_name = #{menuName}</if>"
			+ "<if test ='menuId != null'> and menu_id = #{menuId}</if>"
			+ "<if test ='parentId != null'> and parent_id = #{parentId}</if>"
			+ "<if test ='isFrame != null'> and is_frame = #{isFrame}</if>"
			+ "<if test ='isCache != null'> and is_cache = #{isCache}</if>"
			+ "<if test ='menuType != null'> and menu_type = #{menuType}</if>"
			+ "<if test ='visible != null'> and visible = #{visible}</if>"
			+ "<if test ='status != null'> and status = #{status}</if>"
			+ " order by order_num"
			+ "</script>")
	List<SysMenuEntity> findTreeselect(SysMenuEntity entity);

	@Select("select menu_id from sys_menu where menu_id in(select menu_id from sys_role_menu where role_id=#{roleId})")
	List<Integer> findRoleMenu(@Param("roleId")Integer roleId);

}
