package com.polymeric.dao.merchants;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import com.polymeric.response.api.PoloMerchantBankcardRes;

@Repository
public interface MerchantsCardDao extends BaseMapper<MerchantsCardEntity>{

	@Select("select * from merchants_card where mch_id = #{mchId} and card_state = #{cardState}")
	List<PoloMerchantBankcardRes> findMchId(@Param("mchId") Integer mchId,@Param("cardState") Integer cardState);

	@Select("select " +
			"mc.*," +
			"cc.apply_fee as channelApplyFee," +
			"cc.recharge_fee as channelRechargeFee," +
			"cc.active_min_limit as channelActiveMinLimit," +
			"cc.recharge_min_limit as channelRechargeMinLimit, " +
			"mi.merchants_namme AS mchName " +
			"from merchants_card mc " +
			"left join channel_card cc on mc.card_id = cc.card_id " +
			"left join merchants_info mi on mi.id = mc.mch_id " +
			"where mc.mch_id = #{id} and mc.card_state = 1")
    List<MerchantsCardEntity> selectListAll(@Param("id") Integer id);

	@Select("<script>" +
			"SELECT mc.*, mi.merchants_namme AS mchName " +
			"FROM merchants_card mc " +
			"LEFT JOIN merchants_info mi ON mi.id = mc.mch_id " +
			"<where>" +
			"  <if test='entity.bankCardNature != null and entity.bankCardNature != \"\"'> AND mc.bank_card_nature = #{entity.bankCardNature} </if>" +
			"  <if test='entity.cardBrand != null and entity.cardBrand != \"\"'> AND mc.card_brand = #{entity.cardBrand} </if>" +
			"  <if test='entity.cardState != null'> AND mc.card_state = #{entity.cardState} </if>" +
			"  <if test='entity.ccy != null and entity.ccy != \"\"'> AND mc.ccy = #{entity.ccy} </if>" +
			"  <if test='entity.mchAppid != null and entity.mchAppid != \"\"'> AND mc.mch_appid = #{entity.mchAppid} </if>" +
			"  <if test='entity.mchName != null and entity.mchName != \"\"'> AND mi.merchants_namme LIKE CONCAT('%', #{entity.mchName}, '%') </if>" +
			"  <if test='entity.mchId != null and entity.mchId != \"\"'> AND mc.mch_id = #{entity.mchId}  </if>" +
			"</where>" +
			"</script>")
	List<MerchantsCardEntity> selectCardList(@Param("entity") MerchantsCardEntity entity);

	@Select("select " +
			"mc.*," +
			"cc.apply_fee as channelApplyFee," +
			"cc.recharge_fee as channelRechargeFee," +
			"cc.active_min_limit as channelActiveMinLimit," +
			"cc.recharge_min_limit as channelRechargeMinLimit, " +
			"mi.merchants_namme AS mchName " +
			"from merchants_card mc " +
			"left join channel_card cc on mc.card_id = cc.card_id " +
			"left join merchants_info mi on mi.id = mc.mch_id " +
			"where mc.id = #{id}")
	MerchantsCardEntity selectInfoById(@Param("id") Integer id);

	@Select("select channel_card_id from merchants_card where mch_id = #{id}")
	List<Integer> selectListById(@Param("id") Integer id);

	@Update("update merchants_card set card_state = #{cardStatus} where card_id = #{cardId}")
	void updateByCardId(@Param("cardId") Integer cardId, @Param("cardStatus") Integer cardStatus);

}
