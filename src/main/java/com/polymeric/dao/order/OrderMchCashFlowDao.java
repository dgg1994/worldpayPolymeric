package com.polymeric.dao.order;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.order.OrderMchCashFlowEntity;

@Repository
public interface OrderMchCashFlowDao extends BaseMapper<OrderMchCashFlowEntity>{

	@Select("select * from order_mch_cash_flow where user_bankcard_id = #{userBankcardId} and order_type = #{orderType} and order_state = #{orderState}")
	OrderMchCashFlowEntity findOpenCardList(@Param("userBankcardId") Integer userBankcardId,@Param("orderType") Integer orderType,@Param("orderState") Integer orderState);

	@Select("select * from order_mch_cash_flow where order_num = #{orderNum}")
	OrderMchCashFlowEntity findOrderNum(@Param("orderNum") String orderNum);

}
