package com.polymeric.dao.merchants;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsUserEntity;

@Repository
public interface MerchantsUserDao extends BaseMapper<MerchantsUserEntity>{

	@Select("select * from merchants_user where api_uid = #{uid}")
	MerchantsUserEntity findByUid(@Param("uid") String uid);

}
