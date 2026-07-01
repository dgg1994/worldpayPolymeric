package com.polymeric.dao.merchants;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import com.polymeric.response.polo.PoloMerchantBankcardRes;

@Repository
public interface MerchantsCardDao extends BaseMapper<MerchantsCardEntity>{

	@Select("select * from merchants_card where mch_id = #{mchId} and card_state = #{cardState}")
	List<PoloMerchantBankcardRes> findMchId(@Param("mchId") Integer mchId,@Param("cardState") Integer cardState);

}
