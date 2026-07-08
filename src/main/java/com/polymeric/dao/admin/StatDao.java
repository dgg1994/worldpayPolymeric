package com.polymeric.dao.admin;

import com.polymeric.query.admin.StatQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 首页统计数据
 */
@Repository
public interface StatDao {

    @Select("<script>" +
            "SELECT IFNULL(SUM(channel_amount), 0) FROM order_sys_income_list " +
            "WHERE order_state = 2" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumChannelMoney(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT IFNULL(SUM(actual_amount), 0) FROM order_mch_cash_flow " +
            "WHERE order_state = 2 AND order_type = 3" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumMerchantRechargeTotal(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT IFNULL(SUM(order_amount), 0) FROM order_bankcard_trade_list " +
            "WHERE order_state = 2 AND trade_type = 150" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumBankCardRechargeTotal(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT IFNULL(SUM(order_amount), 0) FROM order_bankcard_trade_list " +
            "WHERE order_state = 2 AND trade_type = 170" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumBankCardConsumeTotal(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT IFNULL(SUM(order_amount), 0) FROM order_bankcard_trade_list " +
            "WHERE order_state = 2 AND trade_type = 140" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumBankCardOpenTotal(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT IFNULL(SUM(profit_amount), 0) FROM order_sys_income_list " +
            "WHERE order_state = 2" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumPlatformIncomeTotal(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT IFNULL(SUM(actual_amount), 0) FROM order_mch_cash_flow " +
            "WHERE order_state = 2" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumTransactionHistory(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT IFNULL(SUM(actual_amount), 0) FROM order_mch_cash_flow " +
            "WHERE order_state = 2 AND trade_type = 2" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumChargeAmount(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT IFNULL(SUM(actual_amount), 0) FROM order_mch_cash_flow " +
            "WHERE order_state = 2 AND trade_type = 1" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    BigDecimal sumRecordAmount(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT COUNT(1) FROM merchants_user WHERE 1 = 1" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    Long countUserRegister(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT COUNT(1) FROM merchants_user_card WHERE 1 = 1" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    Long countBankCardOpen(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT COUNT(1) FROM merchants_user_card " +
            "WHERE card_state &gt;= 3 AND card_state != 7" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    Long countBankCardActive(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT COUNT(1) FROM order_bankcard_trade_list WHERE 1 = 1" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    Long countApiRequest(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT COUNT(1) FROM order_bankcard_trade_list WHERE trade_type = 175" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    Long countApiDelete(@Param("statQuery") StatQuery statQuery);

    @Select("<script>" +
            "SELECT COUNT(1) FROM merchants_webhook_msg WHERE 1 = 1" +
            "<if test='statQuery.merchantAppId != null and statQuery.merchantAppId != \"\"'> AND mch_appid = #{statQuery.merchantAppId} </if>" +
            "<if test='statQuery.startTime != null and statQuery.endTime != null'> AND setTime BETWEEN #{statQuery.startTime} AND #{statQuery.endTime} </if>" +
            "</script>")
    Long countCallBack(@Param("statQuery") StatQuery statQuery);
}
