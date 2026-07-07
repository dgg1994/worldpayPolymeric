package com.polymeric.dao.channel;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.channel.ChannelInfoEntity;

@Repository
public interface ChannelInfoDao extends BaseMapper<ChannelInfoEntity>{

    @Select("select id from channel_info where channel_code = #{channelCode} and app_Id = #{appId}")
    Integer selectByAppIdAndChannelCode(@Param("channelCode") String channelCode, @Param("appId") String appId);

    @Select("select * from channel_info where id = (select channel_id from merchants_info where id = #{mchId})")
	ChannelInfoEntity findMchId(@Param("mchId") Integer mchId);
}
