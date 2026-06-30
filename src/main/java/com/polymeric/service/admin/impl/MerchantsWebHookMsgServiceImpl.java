package com.polymeric.service.admin.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsKeyDao;
import com.polymeric.dao.merchants.MerchantsWebHookMsgDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsKeyEntity;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;
import com.polymeric.enums.WebHookStateEnum;
import com.polymeric.response.pub.ApiResponseEntity;
import com.polymeric.service.admin.MerchantsWebHookMsgService;
import com.polymeric.utils.CallbackHttpSendUtil;
import com.polymeric.utils.sign.RsaSignUtil;

import lombok.Synchronized;

@RestController
@Transactional
@CrossOrigin
public class MerchantsWebHookMsgServiceImpl extends BaseApiService implements MerchantsWebHookMsgService{
	
	@Autowired
	private MerchantsWebHookMsgDao merchantsWebHookMsgDao;
	
	@Autowired
	private MerchantsInfoDao merchantsInfoDao;
	
	@Autowired
	private MerchantsKeyDao merchantsKeyDao;
	
	private static final int[] RETRY_INTERVALS = {60,90,120,180,300,360,420,480,600,600};

	@Override
	public ResponseBase findList(@RequestBody MerchantsWebHookMsgEntity entity) {
		try {
			if(entity.getSysAccountId() != null) {
				MerchantsInfoEntity infoEntity = merchantsInfoDao.findBySysAccountId(entity.getSysAccountId());
				if(infoEntity != null) {
					entity.setMchAppid(infoEntity.getAppId());				
				}
			}
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<MerchantsWebHookMsgEntity> list = merchantsWebHookMsgDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setStatusName(WebHookStateEnum.getName(list.get(i).getStatus()));
				}
			}
			PageInfo<MerchantsWebHookMsgEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
	        throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase send(Integer id) {
		try {
			MerchantsWebHookMsgEntity entity = merchantsWebHookMsgDao.selectById(id);
			if(entity != null) {
				boolean temp = this.callbackMerchants(entity);
				if(temp) {
					return setResultSuccess();
				}
				return setResultError(Constants.ERROR);
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
	        throw new RuntimeException();
		}
	}
	
	
	/**
	 * @category 回调商户
	 * @param msgEntity
	 * @return
	 */
	@Synchronized
	public boolean callbackMerchants(MerchantsWebHookMsgEntity msgEntity) {
		if(msgEntity.getCallbackData() == null || msgEntity.getCallbackData().isEmpty()) {
			return false;
		}
		JSONObject jsonObject = JSONObject.parseObject(msgEntity.getCallbackData());
		//回调参数添加sign
		MerchantsKeyEntity keyEntity = merchantsKeyDao.findAppId(msgEntity.getMchAppid());
		if(keyEntity != null && keyEntity.getPrivateKey() != null && !keyEntity.getPrivateKey().isEmpty()) {
			String sign = RsaSignUtil.signRequest(null,null,null,msgEntity.getCallbackData(), keyEntity.getPrivateKey());
			jsonObject.put("sign", sign);
		}
		//回调商户
		boolean callbackState = false;
		ApiResponseEntity responseEntity = CallbackHttpSendUtil.forwardData(msgEntity.getCallbackUrl(), JSON.toJSONString(jsonObject));
		if(Constants.HTTP_RES_CODE_200.equals(responseEntity.getCode())) {//响应成功
			msgEntity.setStatus(WebHookStateEnum.SUCCESS.getCode());
			callbackState = true;
		}else {
			int retryCount = msgEntity.getRetryCount();
			if (retryCount < RETRY_INTERVALS.length) {
			    int interval = RETRY_INTERVALS[retryCount];
			    Date nextTime = new Date(System.currentTimeMillis() + interval * 1000L);
			    msgEntity.setNextRetryTime(nextTime);
			}
			msgEntity.setStatus(WebHookStateEnum.FAILED.getCode());
		}
		//修改回调消息状态及通知次数
		msgEntity.setRetryCount(msgEntity.getRetryCount() +1);
		msgEntity.setGmtModified(new Date());
		merchantsWebHookMsgDao.updateById(msgEntity);
		return callbackState;
	
	}
	

}
