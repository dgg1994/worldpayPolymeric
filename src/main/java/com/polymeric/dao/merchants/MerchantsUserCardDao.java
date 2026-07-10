package com.polymeric.dao.merchants;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;

import java.util.List;

@Repository
public interface MerchantsUserCardDao extends BaseMapper<MerchantsUserCardEntity>{

	@Select("select * from merchants_user_card where user_bankcard_id = #{userBankcardId}")
	MerchantsUserCardEntity findUserBankcardId(@Param("userBankcardId") Integer userBankcardId);

	@Select("<script>" +
			"SELECT * FROM merchants_user_card" +
			"<where>" +
			"   <if test='entity.mchId != null'>" +
			"       AND mch_id = #{entity.mchId}" +
			"   </if>" +
			"   <if test='entity.userUid != null'>" +
			"       AND user_uid = #{entity.userUid}" +
			"   </if>" +
			"   <if test='entity.cardType != null'>" +
			"       AND card_type = #{entity.cardType}" +
			"   </if>" +
			"   <if test='entity.cardState != null'>" +
			"       AND card_state = #{entity.cardState}" +
			"   </if>" +
			"   <if test='entity.mchAppid != null'>" +
			"       AND mch_appid = #{entity.mchAppid}" +
			"   </if>" +
			"</where>" +
			"</script>")
    List<MerchantsUserCardEntity> selectAll(@Param("entity") MerchantsUserCardEntity entity);
}
