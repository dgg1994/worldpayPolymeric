package com.polymeric.service.admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsKeyDao;
import com.polymeric.dao.merchants.MerchantsWebHookMsgDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;
import com.polymeric.enums.WebHookStateEnum;
import com.polymeric.service.admin.MerchantsWebHookMsgService;
import com.polymeric.service.api.impl.ApiMchWebhook;
import com.polymeric.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

	@Autowired
	private TokenUtils tokenUtils;
	
	private static final int[] RETRY_INTERVALS = {60,90,120,180,300,360,420,480,600,600};

	@Override
	public ResponseBase findList(@RequestBody MerchantsWebHookMsgEntity entity) {
		try {
			if (!tokenUtils.isAdmin()){
				entity.setMchAppid(tokenUtils.getMerchantAppId());
			}
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
				boolean temp = ApiMchWebhook.callbackMerchants(entity);
				if(temp) {
					return setResultSuccess();
				}
				return setResultError(Constants.ERROR);
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
	        return setResultError(Constants.ERROR);
		}
	}
	


}
