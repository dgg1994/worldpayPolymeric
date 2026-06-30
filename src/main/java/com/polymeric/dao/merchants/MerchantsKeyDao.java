package com.polymeric.dao.merchants;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsKeyEntity;

@Repository
public interface MerchantsKeyDao extends BaseMapper<MerchantsKeyEntity>{

	@Select("select * from merchants_key where app_id = #{appid}")
	MerchantsKeyEntity findAppId(@Param("appid") String appid);

	@Select("select * from merchants_key where merchants_id = #{merchantsId}")
	MerchantsKeyEntity findMerchantsId(@Param("merchantsId") Integer merchantsId);

}
