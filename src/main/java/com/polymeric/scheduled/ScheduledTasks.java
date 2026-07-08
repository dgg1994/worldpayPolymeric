package com.polymeric.scheduled;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.channel.PoloConfig;
import com.polymeric.config.channel.PoloMethods;
import com.polymeric.config.channel.UnifiedConfig;
import com.polymeric.constants.Constants;
import com.polymeric.dao.channel.ChannelInfoDao;
import com.polymeric.dao.merchants.MerchantsUserDao;
import com.polymeric.dao.merchants.MerchantsWebHookMsgDao;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.entity.merchants.MerchantsUserEntity;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;
import com.polymeric.enums.KycStateEnums;
import com.polymeric.enums.WebHookStateEnum;
import com.polymeric.response.polo.KycStateRes;
import com.polymeric.service.api.impl.ApiCheck;
import com.polymeric.service.api.impl.ApiMchWebhook;
import com.polymeric.utils.sign.ApiPoloUtil;


@Component
public class ScheduledTasks {
	
	@Autowired
	private MerchantsWebHookMsgDao merchantsWebHookMsgDao;
	
	@Autowired
	private MerchantsUserDao merchantsUserDao;
	
	@Autowired
	private ChannelInfoDao channelInfoDao;
	

	/**
	 * @category 商户回调重试
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void callbackMch() {
		try {
			System.out.println("*********************重试回调给商户**********************");
			List<MerchantsWebHookMsgEntity> list = merchantsWebHookMsgDao.findPendingRetry(new Date(),WebHookStateEnum.FAILED.getCode());
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ApiMchWebhook.callbackMerchants(list.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	
	
	@Scheduled(cron = "0 */2 * * * ?")
	public void findKycState() {
		try {
			System.out.println("*********************kyc状态处理**********************");
			List<MerchantsUserEntity> list = merchantsUserDao.findKycState(KycStateEnums.PROCESS_APPROVE.getIndex());
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ChannelInfoEntity infoEntity = channelInfoDao.selectById(list.get(i).getChannelId());
					UnifiedConfig config = ApiCheck.getConfig(
							infoEntity, 
							PoloConfig.API_URL, 
							PoloConfig.APP_ID,
							PoloConfig.RSA_PRIVATE_KEY, 
							PoloConfig.AES_KEY, 
							PoloMethods.KYC_STATUS);
					// 调用三方接口
					ResponseBase base = ApiPoloUtil.postData(list.get(i).getApiUid(), null, null, config);
					if(Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
						KycStateRes kycStateRes = JSONObject.parseObject(JSON.toJSONString(base.getData()), KycStateRes.class);
						if(kycStateRes != null && KycStateEnums.SUCCESS_APPROVE.getLable().equals(kycStateRes.getStatus())) {//审核通过
							list.get(i).setKycState(KycStateEnums.SUCCESS_APPROVE.getIndex());
						}else if(kycStateRes != null && KycStateEnums.ERROR_APPROVE.getLable().equals(kycStateRes.getStatus())) {
							list.get(i).setKycState(KycStateEnums.ERROR_APPROVE.getIndex());
							list.get(i).setFailedReason(kycStateRes.getFailedReason());
						}
						merchantsUserDao.updateById(list.get(i));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	

}
