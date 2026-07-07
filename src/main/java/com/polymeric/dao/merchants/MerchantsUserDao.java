package com.polymeric.dao.merchants;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsUserEntity;

import java.util.List;

@Repository
public interface MerchantsUserDao extends BaseMapper<MerchantsUserEntity>{

	@Select("select * from merchants_user where api_uid = #{uid}")
	MerchantsUserEntity findByUid(@Param("uid") String uid);

	@Select("<script>" +
			"select mu.*,ci.channel_name as channelName,mi.merchants_namme as merchantsName from merchants_user mu " +
			"left join channel_info ci on mu.channel_id = ci.id " +
			"left join merchants_info mi on mu.mch_id = mi.id " +
			"where 1=1 " +
			"<if test='entity.apiUid != null and entity.apiUid != \"\"'>" +
			"and mu.api_uid = #{entity.apiUid} " +
			"</if>" +
			"<if test='entity.userEmail != null and entity.userEmail != \"\"'>" +
			"and mu.user_email = #{entity.userEmail} " +
			"</if>" +
			"<if test='entity.channelName != null'>" +
			"and ci.channel_name = #{entity.channelName} " +
			"</if>" +
			"<if test='entity.mchId != null'>" +
			"and mu.mch_id = #{entity.mchId} " +
			"</if>" +
			"<if test='entity.merchantsName != null'>" +
			"and mi.merchants_namme = #{entity.merchantsName} " +
			"</if>" +
			"</script>")
	List<MerchantsUserEntity> selectAll(@Param("entity") MerchantsUserEntity entity);
}
