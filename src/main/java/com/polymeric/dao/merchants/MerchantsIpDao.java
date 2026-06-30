package com.polymeric.dao.merchants;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsIpEntity;

@Repository
public interface MerchantsIpDao extends BaseMapper<MerchantsIpEntity>{

	@Select("select * from merchants_ip where app_id = #{appid} and ip_address = #{ipAddress}")
	MerchantsIpEntity findAppIdIp(@Param("appid") String appid,@Param("ipAddress") String ipAddress);

	@Select("select * from merchants_ip where merchants_id = #{merchantsId}")
	List<MerchantsIpEntity> findByMerchantsId(@Param("merchantsId") Integer merchantsId);

	@Select("<script>"
			+ "select * from merchants_ip where 1=1"
			+ "<if test = 'merchantsId'> and merchants_id = #{merchantsId}</if>"
			+ "<if test = 'appId'> and app_id = #{appId}</if>"
			+ "<if test = 'ipAddress'> and ip_address = #{ipAddress}</if>"
			+ "order by setTime desc"
			+ "</script>")
	List<MerchantsIpEntity> findList(MerchantsIpEntity entity);

	@Delete("delete from merchants_ip where app_id = #{appId}")
	void deleteByAppid(@Param("appId") String appId);

}
