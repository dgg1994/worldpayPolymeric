package com.polymeric.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.system.SysRoleEntity;
import com.polymeric.entity.system.SysUserEntity;
import com.polymeric.entity.system.SysUserRoleEntity;

@Repository
public interface SysUserDao extends BaseMapper<SysUserEntity>{

    @Select("select perms from sys_menu where perms is not null and perms != '' and menu_id in "
            + "(select menu_id from sys_role_menu where role_id in "
            + "(select role_id from sys_user_role where user_id = "
            + "(select id from sys_user where acctive = #{acctive} and user_state = #{userState})))")
    List<String> findMenuCodeByUserName(@Param("acctive") String acctive,@Param("userState") Integer userState);

    @Select("select * from sys_user where  acctive = #{acctive} || tel =#{tel}") //user_state = #{state} and
    List<SysUserEntity> findByAcctiveAndTel(@Param("acctive") String acctive,@Param("tel")String tel);//,@Param("state") Integer state

    /**
     * 查找全部未标记为注销状态的用户
     *
     * @param entity
     * @return
     */
    @Select("<script>"
            + "select * from sys_user where 1=1 and user_state = 1 "
            + "<if test = 'deleteState != null' > and user_state != #{deleteState}</if>"
            + "<if test = 'adminRole != null'> and id not in (select user_id from sys_user_role where role_id = #{adminRole})</if>"
            + "<if test = 'username != null'> and username like '%${username}%' </if>"
            + "<if test = 'tel != null'> and tel = #{tel}</if>"
            + "<if test = 'roleId != null'> and id in (select user_id from sys_user_role where role_id in "
            + "<foreach item='item' index='index' collection='roleId' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + ")</if>"
            + "order by user_state asc, create_time desc "
            + "</script>")
    List<SysUserEntity> findNotDeleteAll(SysUserEntity entity);

    @Insert("insert into sys_user_role(user_id,role_id) values(#{user_id},#{role_id})")
    void addUserOrRole(@Param("user_id") Integer user_id, @Param("role_id") Integer role_id);

    @Update("update sys_user set password=#{pwd} where id =#{userId}")
    int resetUserPwd(@Param("userId") Integer userId,@Param("pwd") String pwd);
    
    @Delete("delete from sys_user_role where user_id =#{userId}")
    void delteRole(@Param("userId") Integer userId);

    @Select("select * from sys_role where role_id in(select role_id from sys_user_role where user_id = #{id})")
    List<SysRoleEntity> findUserRole(@Param("id") Integer id);


    @Update("update sys_user set user_state=#{state} where id=#{userId}")
    int updateState(@Param("userId") Integer userId, @Param("state") int state);

    @Select("select * from sys_user where id = #{id}")
    SysUserEntity findById(@Param("id") Integer id);

    @Select("select role_key from sys_role where role_id in(select role_id from sys_user_role where user_id = #{id})")
	List<String> findRoleKey(@Param("id")Integer id);

    @Select("select perms from sys_menu where perms is not null and perms != '' and menu_id in "
            + "(select menu_id from sys_role_menu where role_id in "
            + "(select role_id from sys_user_role where user_id = "
            + "(select id from sys_user where id = #{id})))")
	List<String> findPermissions(@Param("id")Integer id);

    @Select("select role_id from sys_user_role where user_id = #{userId}")
	List<SysUserRoleEntity> findUserRoleJoin(@Param("userId")Integer userId);

    @Select("select perms from sys_menu where perms is not null and perms != '' ")
	List<String> findMenuPermsAll();

    @Select("select * from sys_user where user_state = #{state} and id != #{id} and ( acctive = #{acctive} || tel =#{tel})")
	List<SysUserEntity> findByNoAcctiveAndTel(@Param("acctive")String acctive, @Param("tel")String tel, @Param("id")Integer id, @Param("state")Integer state);

    @Select("select * from sys_user where acctive = #{acctive} and user_state = #{userState}")
	SysUserEntity findByAcctiveState(@Param("acctive")String acctive,@Param("userState") Integer userState);

    @Select("select group_concat(role_name) from sys_role where role_id in(select role_id from sys_user_role where user_id = #{userId})")
	String findUserRoleName(@Param("userId")Integer userId);
    
    @Select("select ifnull(username,'') from sys_user where id = #{id}")
   	String findName(@Param("id") Integer id);

    @Select("select role_id from sys_role where role_id in(select role_id from sys_user_role where user_id = #{userId})")
	List<Integer> findUserRoleId(@Param("userId")Integer userId);

    @Select("<script>"
    		+ "select * from sys_user where 1=1 "
    		+ "<if test = 'roleId != null'> and id in (select user_id from sys_user_role where role_id in "
            + "<foreach item='item' index='index' collection='roleId' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + ")"
            + "</if>"
    		+ "<if test = 'deptId != null'> and id = (select duty_man_id from sys_dept where id = #{deptId})</if>"
    		+ "<if test = 'manageId != null'> and ( id in (select duty_man_id from sys_dept where id in"
    		+ " (select dept_id from sys_manage_dept where manage_id = #{manageId}))"
    		+ " || id in (select duty_man_id from sys_manage where id = #{manageId}))</if>"
    		+ "</script>")
	List<SysUserEntity> selectBook(SysUserEntity entity);


    @Update("update sys_user set password = #{password} where id = #{userId}")
    void updatePwd(@Param("userId") int userId, @Param("password") String password);

    @Select("select * from sys_user where acctive = #{acctive}")
	SysUserEntity findByAcctive(@Param("acctive") String acctive);

}

