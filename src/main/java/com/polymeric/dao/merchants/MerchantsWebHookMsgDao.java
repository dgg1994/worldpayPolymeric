package com.polymeric.dao.merchants;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;

@Repository
public interface MerchantsWebHookMsgDao extends BaseMapper<MerchantsWebHookMsgEntity>{

	@Select("select * from merchants_webhook_msg where sys_orderno = #{sysOrderNo}")
	MerchantsWebHookMsgEntity findOrderNum(@Param("sysOrderNo") String sysOrderNo);

	@Select("SELECT * FROM merchants_webhook_msg " +
            "WHERE status = #{status} " +
            "AND next_retry_time <= #{currentTime}" +
            "AND retry_count < 10 " +
            "ORDER BY next_retry_time ASC")
	List<MerchantsWebHookMsgEntity> findPendingRetry(@Param("currentTime") Date date, @Param("status") Integer status);

	@Select("<script>"
			+ "select * from merchants_webhook_msg where  1=1"
			+ "<if test ='mchAppid != null'> and mch_appid = #{mchAppid}</if>"
			+ "<if test ='sysOrderNo != null'> and sys_orderno = #{sysOrderNo}</if>"
			+ "<if test ='merchantOrderNo != null'> and merchant_order_no = #{merchantOrderNo}</if>"
			+ "<if test ='status != null'> and status = #{status}</if>"
			+ "<if test = 'startTime != null'> and DATE_FORMAT(setTime, '%Y-%m-%d') &gt;= #{startTime}</if> "
			+ "<if test = 'endTime != null'> and DATE_FORMAT(setTime, '%Y-%m-%d') &lt;= #{endTime}</if> "
			+ " order by setTime desc"
			+ "</script>")
	List<MerchantsWebHookMsgEntity> findList(MerchantsWebHookMsgEntity entity);

}
