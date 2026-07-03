package com.polymeric.service.api.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.channel.PoloConfig;
import com.polymeric.config.channel.PoloMethods;
import com.polymeric.config.channel.UnifiedConfig;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsUserCardDao;
import com.polymeric.dao.order.OrderBankCardTradeDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;
import com.polymeric.entity.order.OrderBankCardTradeEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.query.api.ApiAuthApproveQuery;
import com.polymeric.query.api.ApiRecordInfoQuery;
import com.polymeric.query.api.ApiRecordQuery;
import com.polymeric.response.api.BankCardInfoRes;
import com.polymeric.service.api.ApiTradeService;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.sign.ApiPoloUtil;

@RestController
@Transactional
@CrossOrigin
public class ApiTradeServiceImpl extends BaseApiService implements ApiTradeService{
	
	@Autowired
	private MerchantsUserCardDao merchantsUserCardDao;
	
	@Autowired
	private OrderBankCardTradeDao orderBankCardTradeDao;

	@Override
	public ResponseBase record(HttpServletRequest request, @Valid @RequestBody ApiRecordQuery recordQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, recordQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloRecord(infoEntity,recordQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 查询银行卡交易记录
	 * @param infoEntity
	 * @param recordQuery
	 * @return
	 */
	public ResponseBase poloRecord(MerchantsInfoEntity infoEntity, @Valid ApiRecordQuery recordQuery) {
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		// 判断银行卡是否存在
		MerchantsUserCardEntity userCardEntity = merchantsUserCardDao.findUserBankcardId(recordQuery.getUserBankcardId());
		if (userCardEntity == null) {
			return setResultError(ErrorCodeEnum.USER_BANK_CARD_ERROR.getCode(),I18nUtil.getMessage("user_bank_card_error"));
		}
		PageHelper.startPage(recordQuery.getPageNumber(), recordQuery.getPageSize());
		List<BankCardInfoRes> list = orderBankCardTradeDao.findList(recordQuery);
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase recordInfo(HttpServletRequest request, @Valid @RequestBody ApiRecordInfoQuery infoQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, infoQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloRecordInfo(infoEntity,infoQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 银行卡交易记录详情
	 * @param infoEntity
	 * @param infoQuery
	 * @return
	 */
	public ResponseBase poloRecordInfo(MerchantsInfoEntity infoEntity, @Valid ApiRecordInfoQuery infoQuery) {
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		OrderBankCardTradeEntity infoRes = orderBankCardTradeDao.selectById(infoQuery.getId());
		BankCardInfoRes entity = new BankCardInfoRes();
		BeanUtils.copyProperties(infoRes, entity);
		return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase authApprove(HttpServletRequest request, @Valid @RequestBody ApiAuthApproveQuery authApproveQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, authApproveQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloAuthApprove(infoEntity,authApproveQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 3ds授权通过
	 * @param infoEntity
	 * @param authApproveQuery
	 * @return
	 */
	public ResponseBase poloAuthApprove(MerchantsInfoEntity infoEntity, @Valid ApiAuthApproveQuery authApproveQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(),
				PoloConfig.API_URL, 
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.BANKCARD_3DS_APPROVE);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, authApproveQuery,config);
		return base;
		
	}

	@Override
	public ResponseBase authReject(HttpServletRequest request, @Valid @RequestBody ApiAuthApproveQuery authApproveQuery) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, authApproveQuery);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloAuthReject(infoEntity,authApproveQuery);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 3ds授权拒绝
	 * @param infoEntity
	 * @param authApproveQuery
	 * @return
	 */
	public ResponseBase poloAuthReject(MerchantsInfoEntity infoEntity, @Valid ApiAuthApproveQuery authApproveQuery) {
		// 获取上游配置
		UnifiedConfig config = ApiCheck.getConfig(
				infoEntity.getChannelData(), 
				PoloConfig.API_URL,
				PoloConfig.APP_ID,
				PoloConfig.RSA_PRIVATE_KEY, 
				PoloConfig.AES_KEY, 
				PoloMethods.BANKCARD_3DS_REJECT);
		if (infoEntity.getMerchantsUserData() == null) {
			return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
		}
		ResponseBase base = ApiPoloUtil.postData(infoEntity.getMerchantsUserData().getApiUid(), null, authApproveQuery,
				config);
		return base;
	}

}
