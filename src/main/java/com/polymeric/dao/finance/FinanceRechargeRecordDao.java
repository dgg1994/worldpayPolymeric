package com.polymeric.dao.finance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.finance.FinanceRechargeRecordEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类描述：财务充值记录
 *
 * @author GeminiSun
 * @date 2026/07/02 14:28
 */
@Repository
public interface FinanceRechargeRecordDao extends BaseMapper<FinanceRechargeRecordEntity> {

    @Select("<script>" +
            "select frr.*, mi.merchants_namme as merchantName " +
            "from finance_recharge_record frr " +
            "left join merchants_info mi on mi.id = frr.merchant_id " +
            "<where> " +
            "   <if test='entity.merchantName != null and entity.merchantName != \"\"'> " +
            "       and mi.merchants_namme like concat('%', #{entity.merchantName}, '%') " +
            "   </if> " +
            "   <if test='entity.txStatus != null and entity.txStatus != \"\"'> " +
            "       and frr.tx_status = #{entity.txStatus} " +
            "   </if> " +
            "   <if test='entity.merchantId != null and entity.merchantId != \"\"'> " +
            "       and frr.merchant_id = #{entity.merchantId} " +
            "   </if> " +
            "</where> " +
            "order by frr.setTime desc " +
            "</script>")
    List<FinanceRechargeRecordEntity> selectAll(@Param("entity")FinanceRechargeRecordEntity entity);
}
