package com.polymeric.service.api.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.channel.PoloConfig;
import com.polymeric.config.channel.PoloMethods;
import com.polymeric.config.channel.UnifiedConfig;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsCardDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.enums.PublicEnums;
import com.polymeric.response.polo.PoloMerchantBankcardRes;
import com.polymeric.service.api.ApiBankCardService;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.sign.ApiPoloUtil;

@RestController
@Transactional
@CrossOrigin
public class ApiBankCardServiceImpl extends BaseApiService implements ApiBankCardService{
	
	@Autowired
	private MerchantsCardDao merchantsCardDao;

	@Override
	public ResponseBase merchantBankCardList(HttpServletRequest request) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, null);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloMerchantBankcardList(infoEntity);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category polo查询商户可用卡产品列表信息
	 * @param infoEntity
	 * @return
	 */
	public ResponseBase poloMerchantBankcardList(MerchantsInfoEntity infoEntity) {
		List<PoloMerchantBankcardRes> cardList = merchantsCardDao.findMchId(infoEntity.getId(),PublicEnums.ONE.getIndex());
		return setResultSuccess(cardList);
	}

	@Override
	public ResponseBase userCardList(HttpServletRequest request) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, null);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloUserCardList(infoEntity);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 用户银行卡列表
	 * @param infoEntity
	 * @return
	 */
	public ResponseBase poloUserCardList(MerchantsInfoEntity infoEntity) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(),
				PoloConfig.API_URL, 
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY,
				PoloConfig.AES_KEY,
				PoloMethods.USER_CARD_LIST);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 调用三方接口
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, null, config);
		return base;
	}

}
