package com.polymeric.dao.merchants;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;

@Repository
public interface MerchantsUserCardDao extends BaseMapper<MerchantsUserCardEntity>{

	@Select("select * from merchants_user_card where user_bankcard_id = #{userBankcardId}")
	MerchantsUserCardEntity findUserBankcardId(@Param("userBankcardId") Integer userBankcardId);

}
