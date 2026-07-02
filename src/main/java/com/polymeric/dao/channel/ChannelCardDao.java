package com.polymeric.dao.channel;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.channel.ChannelCardEntity;

@Repository
public interface ChannelCardDao extends BaseMapper<ChannelCardEntity>{

	@Select("select * from channel_card where id = (select channel_card_id from merchants_card where id = #{cardId})")
	ChannelCardEntity findMchCardId(@Param("cardId") Integer cardId);

}
