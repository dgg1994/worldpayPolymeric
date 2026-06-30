package com.polymeric.dao.merchants;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsInfoEntity;

@Repository
public interface MerchantsInfoDao extends BaseMapper<MerchantsInfoEntity>{

	@Select("select * from merchants_info where merchants_namme = #{merchantsNamme}")
	MerchantsInfoEntity findByName(@Param("merchantsNamme") String merchantsNamme);

	@Select("<script>"
			+ "select * from merchants_info where 1=1"
			+ "<if test = 'merchantsNamme != null'> and merchants_namme like concat('%', #{merchantsNamme}, '%')</if>"
			+ "<if test = 'appId != null'> and app_id = #{appId}</if>"
			+ "<if test = 'merchantsStatus != null'> and merchants_status = #{merchantsStatus}</if>"
			+ "<if test = 'contactPhone != null'> and contact_phone = #{contactPhone}</if> "
			+ "<if test = 'sysAccountId != null'> and sys_account_id = #{sysAccountId}</if> "
			+ "order by setTime desc"
			+ "</script>")
	List<MerchantsInfoEntity> findList(MerchantsInfoEntity entity);

	@Select("select * from merchants_info where app_id = #{appId}")
	MerchantsInfoEntity findByAppId(@Param("appId") String appId);

	@Select("select * from merchants_info where sys_account_id = #{acctiveId}")
	MerchantsInfoEntity findBySysAccountId(@Param("acctiveId") Integer acctiveId);


	@Select("<script>"
			+ "select ifnull(sum(available_amount),0) as availableAmountTotal, "
			+ "ifnull(sum(beforehand_amount),0) as beforehandAmountTotal,"
			+ "ifnull(sum(finish_amount),0) as finishAmountTotal "
			+ "from merchants_info where 1=1"
			+ "<if test = 'merchantsNamme != null'> and merchants_namme like concat('%', #{merchantsNamme}, '%')</if>"
			+ "<if test = 'appId != null'> and app_id = #{appId}</if>"
			+ "<if test = 'merchantsStatus != null'> and merchants_status = #{merchantsStatus}</if>"
			+ "<if test = 'contactPhone != null'> and contact_phone = #{contactPhone}</if> "
			+ "<if test = 'sysAccountId != null'> and sys_account_id = #{sysAccountId}</if> "
			+ "order by setTime desc"
			+ "</script>")
	MerchantsInfoEntity findSum(MerchantsInfoEntity entity);

}
