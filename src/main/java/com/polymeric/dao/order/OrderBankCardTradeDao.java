package com.polymeric.dao.order;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.order.OrderBankCardTradeEntity;
import com.polymeric.query.api.ApiRecordQuery;
import com.polymeric.response.api.BankCardInfoRes;

@Repository
public interface OrderBankCardTradeDao extends BaseMapper<OrderBankCardTradeEntity>{

	@Select("<script>"
			+ "select * from order_bankcard_trade_list where 1=1"
			+ "<if test = 'userBankcardId != null'> and user_bankcard_id = #{userBankcardId}</if>"
			+ "order by setTime desc"
			+ "</script>")
	List<BankCardInfoRes> findList(@Valid ApiRecordQuery recordQuery);

	@Select("select * from order_bankcard_trade_list where id = #{id}")
	BankCardInfoRes findById(@Param("id") Integer id);

	@Select("select * from order_bankcard_trade_list where user_bankcard_id = #{userBankcardId} and trade_type = #{tradeType} and order_state = #{orderState}")
	OrderBankCardTradeEntity findOpenCardList(@Param("userBankcardId") Integer userBankcardId,@Param("tradeType") Integer tradeType,@Param("orderState") Integer orderState);

	@Select("select * from order_bankcard_trade_list where order_num = #{orderNum}")
	OrderBankCardTradeEntity findOrderNum(@Param("orderNum") String orderNum);

}
