package com.polymeric.dao.order;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.order.OrderMchCashFlowEntity;

import java.util.List;

@Repository
public interface OrderMchCashFlowDao extends BaseMapper<OrderMchCashFlowEntity>{

	@Select("select * from order_mch_cash_flow where user_bankcard_id = #{userBankcardId} and order_type = #{orderType} and order_state = #{orderState}")
	OrderMchCashFlowEntity findOpenCardList(@Param("userBankcardId") Integer userBankcardId,@Param("orderType") Integer orderType,@Param("orderState") Integer orderState);

	@Select("select * from order_mch_cash_flow where order_num = #{orderNum}")
	OrderMchCashFlowEntity findOrderNum(@Param("orderNum") String orderNum);

	@Select("<script>" +
			"SELECT om.*, mi.merchants_namme as mchName " +
			"FROM order_mch_cash_flow om " +
			"LEFT JOIN merchants_info mi ON om.mch_id = mi.id " +
			"<where>" +
			"<if test='entity.mchName != null and entity.mchName != \"\"'>" +
			"AND mi.merchants_namme = #{entity.mchName}" +
			"</if>" +
			"<if test='entity.mchAppid != null and entity.mchAppid != \"\"'>" +
			"AND om.mch_appid = #{entity.mchAppid}" +
			"</if>" +
			"<if test='entity.mchOrderNum != null and entity.mchOrderNum != \"\"'>" +
			"AND om.mch_order_num = #{entity.mchOrderNum}" +
			"</if>" +
			"<if test='entity.orderNum != null and entity.orderNum != \"\"'>" +
			"AND om.order_num = #{entity.orderNum}" +
			"</if>" +
			"<if test='entity.orderState != null and entity.orderState != \"\"'>" +
			"AND om.order_state = #{entity.orderState}" +
			"</if>" +
			"<if test='entity.orderType != null and entity.orderType != \"\"'>" +
			"AND om.order_type = #{entity.orderType}" +
			"</if>" +
			"<if test='entity.tradeType != null and entity.tradeType != \"\"'>" +
			"AND om.trade_type = #{entity.tradeType}" +
			"</if>" +
			"<if test='entity.startTime != null and entity.endTime != null'>" +
			"AND om.setTime BETWEEN #{entity.startTime} AND #{entity.endTime}" +
			"</if>" +
			"</where>" +
			"ORDER BY om.setTime DESC" +
			"</script>")
    List<OrderMchCashFlowEntity> selectAll(@Param("entity") OrderMchCashFlowEntity entity);
}
