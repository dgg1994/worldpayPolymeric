package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.channel.ChannelCardDao;
import com.polymeric.dao.channel.ChannelInfoDao;
import com.polymeric.dao.finance.FinanceRechargeRecordDao;
import com.polymeric.dao.merchants.MerchantsCardDao;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsIpDao;
import com.polymeric.dao.merchants.MerchantsKeyDao;
import com.polymeric.dao.system.SysRoleDao;
import com.polymeric.dao.system.SysUserDao;
import com.polymeric.entity.channel.ChannelCardEntity;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.entity.finance.FinanceRechargeRecordEntity;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsIpEntity;
import com.polymeric.entity.merchants.MerchantsKeyEntity;
import com.polymeric.entity.system.SysRoleEntity;
import com.polymeric.entity.system.SysUserEntity;
import com.polymeric.enums.RecordTypeEnums;
import com.polymeric.enums.RoleTypeEnums;
import com.polymeric.enums.TxStatusEnums;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.query.admin.MerchantsFinanceQuery;
import com.polymeric.response.sign.KeyPairResult;
import com.polymeric.service.admin.MerchantsService;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.GoogleAuthenticatorUtil;
import com.polymeric.utils.sign.KeyPairUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@Transactional
@CrossOrigin
@Slf4j
public class MerchantsServiceImpl extends BaseApiService implements MerchantsService{
	
	@Autowired
	private MerchantsInfoDao merchantsInfoDao;
	
	@Autowired
	private MerchantsKeyDao merchantsKeyDao;
	
	@Autowired
	private MerchantsIpDao merchantsIpDao;

	@Autowired
	private MerchantsCardDao merchantsCardDao;

	@Autowired
	private ChannelCardDao channelCardDao;

	@Autowired
	private ChannelInfoDao channelInfoDao;

	@Autowired
	private FinanceRechargeRecordDao financeRechargeRecordDao;
	
	@Autowired
	SysUserDao sysUserDao;
	
	@Autowired
	private SysRoleDao sysRoleDao;

	@Override
	public ResponseBase add(@RequestBody MerchantsInfoEntity entity) {
		try {
			//校验
			SysUserEntity sysUserEntity = sysUserDao.findByAcctive(entity.getMerchantsAccount());
			if(sysUserEntity != null) {
				return setResultError("账户已存在");
			}
			MerchantsInfoEntity merchantsInfoEntity = merchantsInfoDao.findByName(entity.getMerchantsNamme());
			if(merchantsInfoEntity != null) {
				return setResultError("商户已存在");
			}
			//创建登录账户
			SysUserEntity userEntity = new SysUserEntity();
			userEntity.setAcctive(entity.getMerchantsAccount());
			userEntity.setUsername(entity.getMerchantsNamme());
			userEntity.setTel(entity.getContactPhone());
			userEntity.setUserState(UserStateEnums.NORMAL.getIndex());
			userEntity.setPassword(DigestUtils.md5DigestAsHex(Constants.DEFAULT_PASSWORD.getBytes()));
			String key = GoogleAuthenticatorUtil.createKey(entity.getMerchantsAccount()).getKey();
			userEntity.setGoogleSecretkey(key);
			sysUserDao.insert(userEntity);
			//查询商户角色id
			SysRoleEntity roleEntity = sysRoleDao.findRoleIdByRoleKey(RoleTypeEnums.MERCHANTS.getValue());
			sysUserDao.addUserOrRole(userEntity.getId(), roleEntity.getRoleId());
			//新增商户
			entity.setMerchantsStatus(UserStateEnums.NORMAL.getIndex());
			entity.setSysAccountId(userEntity.getId());
			entity.setAppId(KeyPairUtil.generateAppId());
			GenericityUtil.setDate(entity);
			merchantsInfoDao.insert(entity);
			//创建商户密钥
			MerchantsKeyEntity keyEntity = new MerchantsKeyEntity();
			keyEntity.setAppId(entity.getAppId());
			keyEntity.setMerchantsId(entity.getId());
			KeyPairResult keyPairResult = KeyPairUtil.generateKeyPairResult();
			keyEntity.setPrivateKey(keyPairResult.getPrivateKey());
			keyEntity.setPublicKey(keyPairResult.getPublicKey());
			GenericityUtil.setDate(keyEntity);
			merchantsKeyDao.insert(keyEntity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(@RequestBody MerchantsInfoEntity entity) {
		try {
			MerchantsInfoEntity infoEntity = merchantsInfoDao.selectById(entity.getId());
			if(infoEntity != null) {
				if(!infoEntity.getMerchantsNamme().equals(entity.getMerchantsNamme())) {
					MerchantsInfoEntity merchantsInfoEntity = merchantsInfoDao.findByName(entity.getMerchantsNamme());
					if(merchantsInfoEntity != null) {
						return setResultError("商户已存在");
					}
				}
				SysUserEntity userEntity = sysUserDao.selectById(entity.getSysAccountId());
				if(userEntity != null) {
					if(!userEntity.getAcctive().equals(entity.getMerchantsAccount())) {
						SysUserEntity sysUserEntity = sysUserDao.findByAcctive(entity.getMerchantsAccount());
						if(sysUserEntity != null) {
							return setResultError("账户已存在");
						}
					}
					userEntity.setAcctive(entity.getMerchantsAccount());
					userEntity.setUsername(entity.getMerchantsNamme());
					userEntity.setTel(entity.getContactPhone());
					sysUserDao.updateById(userEntity);
				}
				merchantsInfoDao.updateById(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	public ResponseBase findList(@RequestBody MerchantsInfoEntity entity) {
		try {
			String merchantsAccount = entity.getMerchantsAccount();
			QueryWrapper<MerchantsInfoEntity> wrapper = new QueryWrapper<>();
			if (StringUtils.isNotBlank(merchantsAccount)){
				wrapper.eq("merchants_account",merchantsAccount);
			}
			Integer merchantsStatus = entity.getMerchantsStatus();
			if (merchantsStatus != null){
				wrapper.eq("merchants_status",merchantsStatus);
			}
			wrapper.orderByDesc("setTime");
			List<MerchantsInfoEntity> list = merchantsInfoDao.selectList(wrapper);
			if(list != null && !list.isEmpty()) {
				for (MerchantsInfoEntity merchantsInfoEntity : list) {
					List<MerchantsIpEntity> ipEntities = merchantsIpDao.findByMerchantsId(merchantsInfoEntity.getId());
					merchantsInfoEntity.setIpList(ipEntities);
					MerchantsKeyEntity keyEntity = merchantsKeyDao.findAppId(merchantsInfoEntity.getAppId());
					merchantsInfoEntity.setMerchantsKey(keyEntity);
				}
			}
			List<MerchantsInfoEntity> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
			PageInfo<MerchantsInfoEntity> info = new PageInfo<>(pageList);
			info.setTotal(list.size());
			return setResultSuccess(info, Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase updateState(Integer id,Integer merchantsStatus) {
		try {
			MerchantsInfoEntity entity = merchantsInfoDao.selectById(id);
			if(entity != null) {
				entity.setMerchantsStatus(merchantsStatus);
				merchantsInfoDao.updateById(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase updateKey(Integer id) {
		try {
			MerchantsKeyEntity entity = merchantsKeyDao.findMerchantsId(id);
			if(entity != null) {
				KeyPairResult keyPairResult = KeyPairUtil.generateKeyPairResult();
				entity.setPrivateKey(keyPairResult.getPrivateKey());
				entity.setPublicKey(keyPairResult.getPublicKey());
				merchantsKeyDao.updateById(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findById(Integer id) {
		List<MerchantsCardEntity> merchantsCardEntities = merchantsCardDao.selectListAll(id);
		if (merchantsCardEntities == null){
			merchantsCardEntities = new ArrayList<>();
		}
		return setResultSuccess(merchantsCardEntities,Constants.SUCCESS);
	}

	@Override
	public ResponseBase assignChannel(@RequestParam Integer id, @RequestParam Integer channelId, @RequestParam List<Integer> channelCardsId) throws InvocationTargetException, IllegalAccessException {
		if (id == null) {
			return setResultError("商户id不能为空");
		}
		if (channelId == null) {
			return setResultError("上游id不能为空");
		}
		MerchantsInfoEntity merchantsInfoEntity = merchantsInfoDao.selectById(id);
		if (merchantsInfoEntity == null) {
			return setResultError("商户信息不存在");
		}
		ChannelInfoEntity channelInfoEntity = channelInfoDao.selectById(channelId);
		if (channelInfoEntity == null) {
			return setResultError("上游信息不存在");
		}
		if (!Integer.valueOf(1).equals(channelInfoEntity.getChannelState())) {
			return setResultError("上游已停用，无法分配");
		}
		merchantsInfoEntity.setChannelId(channelInfoEntity.getId());
		merchantsInfoEntity.setChannelCode(channelInfoEntity.getChannelCode());
		merchantsInfoEntity.setGmtModified(new Date());
		merchantsInfoDao.updateById(merchantsInfoEntity);

		if (CollectionUtils.isEmpty(channelCardsId)) {
			return setResultSuccess();
		}
		List<ChannelCardEntity> channelCardList = channelCardDao.selectBatchIds(channelCardsId);
		if (channelCardList.size() != channelCardsId.size()) {
			return setResultError("部分上游卡不存在，请确认卡片信息");
		}
		for (ChannelCardEntity channelCardEntity : channelCardList) {
			if (!channelId.equals(channelCardEntity.getChannelId())) {
				return setResultError("上游卡与指定上游不匹配，cardId：" + channelCardEntity.getId());
			}
			QueryWrapper<MerchantsCardEntity> wrapper = new QueryWrapper<>();
			wrapper.eq("mch_id", id).eq("channel_card_id", channelCardEntity.getId());
			if (merchantsCardDao.selectCount(wrapper) > 0) {
				log.info("商户已分配该上游卡，跳过，mchId：{}，channelCardId：{}", id, channelCardEntity.getId());
				continue;
			}
			MerchantsCardEntity merchantsCardEntity = new MerchantsCardEntity();
			BeanUtils.copyProperties(channelCardEntity, merchantsCardEntity);
			merchantsCardEntity.setId(null);
			merchantsCardEntity.setMchName(merchantsInfoEntity.getMerchantsNamme());
			merchantsCardEntity.setChannelCardId(channelCardEntity.getId());
			merchantsCardEntity.setMchId(merchantsInfoEntity.getId());
			merchantsCardEntity.setMchAppid(merchantsInfoEntity.getAppId());
			merchantsCardEntity.setChannelId(channelInfoEntity.getId());
			merchantsCardEntity.setChannelCode(channelInfoEntity.getChannelCode());
			GenericityUtil.setDate(merchantsCardEntity);
			merchantsCardDao.insert(merchantsCardEntity);
		}
		return setResultSuccess();
	}

	@Override
	public ResponseBase topUp(@RequestBody MerchantsFinanceQuery merchantsFinanceQuery) throws InvocationTargetException, IllegalAccessException {
		Integer mchId = merchantsFinanceQuery.getMchId();
		if (mchId == null){
			return setResultError("商户id不能为空，当前交易无效");
		}
		//获取商户信息
		MerchantsInfoEntity merchantsInfoEntity = merchantsInfoDao.selectById(mchId);
		if (merchantsInfoEntity == null){
			return setResultError("商户不存在，当前交易无效");
		}
		//新增充值记录
		FinanceRechargeRecordEntity entity = new FinanceRechargeRecordEntity();
		entity.setMerchantId(String.valueOf(merchantsFinanceQuery.getMchId()));
		entity.setFinanceAddress(merchantsFinanceQuery.getMerchantsAddress());
		entity.setAmount(merchantsFinanceQuery.getMerchantsAmount());
		entity.setTxStatus(TxStatusEnums.WAIT.getIndex().toString());
		entity.setRecordType(RecordTypeEnums.MANUAL.getIndex().toString());
		entity.setOperator(merchantsFinanceQuery.getOperator());
		GenericityUtil.setDate(entity);
		financeRechargeRecordDao.insert(entity);
		return setResultSuccess();
	}


}
