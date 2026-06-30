package com.polymeric.service.admin.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsIpDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsIpEntity;
import com.polymeric.service.admin.MerchantsIpService;
import com.polymeric.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class MerchantsIpServiceImpl extends BaseApiService implements MerchantsIpService{
	
	@Autowired
	private MerchantsIpDao merchantsIpDao;
	
	@Autowired
	private MerchantsInfoDao merchantsInfoDao;

	@Override
	public ResponseBase findList(@RequestBody MerchantsIpEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<MerchantsIpEntity> list = merchantsIpDao.findList(entity);
			PageInfo<MerchantsIpEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase addOrUpIpWhitelist(@RequestBody MerchantsIpEntity entity) {
		try {
			MerchantsIpEntity ipEntity = merchantsIpDao.findAppIdIp(entity.getAppId(), entity.getIpAddress());
			if(ipEntity != null) {
				return setResultError("ip已配置");
			}
			MerchantsInfoEntity infoEntity = merchantsInfoDao.findByAppId(entity.getAppId());
			if(infoEntity == null) {
				return setResultError(Constants.ERROR);
			}
			if(entity.getIpWhitelist() != null && entity.getIpWhitelist().size() > 0) {
				//删除原有数据
				merchantsIpDao.deleteByAppid(entity.getAppId());
				for (int i = 0; i < entity.getIpWhitelist().size(); i++) {
					MerchantsIpEntity merchantsIpEntity = new MerchantsIpEntity();
					merchantsIpEntity.setAppId(infoEntity.getAppId());
					merchantsIpEntity.setIpAddress(entity.getIpWhitelist().get(i));
					merchantsIpEntity.setMerchantsId(infoEntity.getId());
					GenericityUtil.setDate(merchantsIpEntity);
					merchantsIpDao.insert(merchantsIpEntity);
				}
			}
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase updateIp(@RequestBody MerchantsIpEntity entity) {
		try {
			MerchantsIpEntity ipEntity = merchantsIpDao.selectById(entity.getId());
			if(ipEntity != null) {
				merchantsIpDao.updateById(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase deleteIp(Integer id) {
		try {
			MerchantsIpEntity ipEntity = merchantsIpDao.selectById(id);
			if(ipEntity != null) {
				merchantsIpDao.deleteById(id);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
