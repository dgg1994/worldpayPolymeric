package com.polymeric.dao.order;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.order.OrderIncomeListEntity;

@Repository
public interface OrderIncomeListDao extends BaseMapper<OrderIncomeListEntity>{

	@Select("select * from order_sys_income_list where user_bankcard_id = #{userBankcardId} and order_type = #{orderType} and order_state = #{orderState}")
	OrderIncomeListEntity findOpenCardList(@Param("userBankcardId") Integer userBankcardId,@Param("orderType") Integer orderType,@Param("orderState") Integer orderState);

	@Select("select * from order_sys_income_list where order_num = #{orderNum}")
	OrderIncomeListEntity findOrderNum(@Param("orderNum") String orderNum);

}
