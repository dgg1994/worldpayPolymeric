package com.polymeric.dao.channel;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.channel.ChannelCardEntity;

import java.util.List;

@Repository
public interface ChannelCardDao extends BaseMapper<ChannelCardEntity>{

	@Select("select * from channel_card where id = (select channel_card_id from merchants_card where id = #{cardId})")
	ChannelCardEntity findMchCardId(@Param("cardId") Integer cardId);

	@Select("select cc.*,ci.channel_name as channelName from channel_card cc left join channel_info ci on ci.id = cc.channel_id where cc.id = #{id}")
    ChannelCardEntity selectInfoById(@Param("id") Integer id);

	@Select("<script>" +
			"SELECT cc.*, ci.channel_name AS channelName " +
			"FROM channel_card cc " +
			"LEFT JOIN channel_info ci ON ci.id = cc.channel_id " +
			"<where>" +
			"   <if test='entity.channelId != null and entity.channelId != \"\"'>" +
			"       AND cc.channel_id = #{entity.channelId}" +
			"   </if>" +
			"   <if test='entity.cardTitle != null and entity.cardTitle != \"\"'>" +
			"       AND cc.card_title = #{entity.cardTitle}" +
			"   </if>" +
			"   <if test='entity.bankCardNature != null and entity.bankCardNature != \"\"'>" +
			"       AND cc.bank_card_nature = #{entity.bankCardNature}" +
			"   </if>" +
			"   <if test='entity.cardState != null'>" +
			"       AND cc.card_state = #{entity.cardState}" +
			"   </if>" +
			"</where>" +
			"</script>")
    List<ChannelCardEntity> selectAll(@Param("entity") ChannelCardEntity entity);
}
