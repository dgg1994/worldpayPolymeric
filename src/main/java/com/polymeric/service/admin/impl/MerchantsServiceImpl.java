package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.polymeric.dao.order.OrderMchCashFlowDao;
import com.polymeric.dao.system.SysRoleDao;
import com.polymeric.dao.system.SysUserDao;
import com.polymeric.entity.channel.ChannelCardEntity;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.entity.finance.FinanceRechargeRecordEntity;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsIpEntity;
import com.polymeric.entity.merchants.MerchantsKeyEntity;
import com.polymeric.entity.order.OrderMchCashFlowEntity;
import com.polymeric.entity.system.SysRoleEntity;
import com.polymeric.entity.system.SysUserEntity;
import com.polymeric.enums.*;
import com.polymeric.query.admin.MerchantsFinanceQuery;
import com.polymeric.response.sign.KeyPairResult;
import com.polymeric.service.admin.MerchantsService;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.GoogleAuthenticatorUtil;
import com.polymeric.utils.OrderCodeFactory;
import com.polymeric.utils.TokenUtils;
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
import java.math.BigDecimal;
import java.util.*;


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
	private OrderMchCashFlowDao orderMchCashFlowDao;
	
	@Autowired
	SysUserDao sysUserDao;
	
	@Autowired
	private SysRoleDao sysRoleDao;

	@Autowired
	private TokenUtils tokenUtils;

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
            if (!tokenUtils.isAdmin()) {
				wrapper.eq("merchants_account",tokenUtils.getUsername());
            }
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
		// 1. 参数校验
		if (id == null) return setResultError("商户id不能为空");
		if (channelId == null) return setResultError("上游id不能为空");

		// 2. 基础信息校验
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

		List<Integer> incomingChannelCardIds = CollectionUtils.isEmpty(channelCardsId)
				? Collections.emptyList() : channelCardsId;

		if (merchantsInfoEntity.getChannelId() == null) {
			//更新上游id和编码到商户表
			merchantsInfoEntity.setChannelId(channelInfoEntity.getId());
			merchantsInfoEntity.setChannelCode(channelInfoEntity.getChannelCode());
			merchantsInfoEntity.setGmtModified(new Date());
			merchantsInfoDao.updateById(merchantsInfoEntity);
			if (!incomingChannelCardIds.isEmpty()) {
				addMerchantsCard(incomingChannelCardIds, merchantsInfoEntity, channelInfoEntity);
			}
		} else {
			syncMerchantsCards(incomingChannelCardIds, merchantsInfoEntity, channelInfoEntity);
		}

		return setResultSuccess();
	}

	/**
	 * 同步商户商品：传入比库中多则新增，库中比传入多则禁用，已禁用再次选中则恢复
	 */
	private void syncMerchantsCards(List<Integer> channelCardsId,
	                                MerchantsInfoEntity merchantsInfoEntity,
	                                ChannelInfoEntity channelInfoEntity) throws InvocationTargetException, IllegalAccessException {
		Set<Integer> incomingSet = new HashSet<>(channelCardsId);
		List<MerchantsCardEntity> existingCards = merchantsCardDao.selectListAll(merchantsInfoEntity.getId());
		Map<Integer, MerchantsCardEntity> existingByChannelCardId = new HashMap<>();
		if (!CollectionUtils.isEmpty(existingCards)) {
			for (MerchantsCardEntity card : existingCards) {
				if (card.getChannelCardId() != null) {
					existingByChannelCardId.put(card.getChannelCardId(), card);
				}
			}
		}

		List<Integer> toAddIds = new ArrayList<>();
		for (Integer channelCardId : incomingSet) {
			MerchantsCardEntity existing = existingByChannelCardId.get(channelCardId);
			if (existing == null) {
				toAddIds.add(channelCardId);
				continue;
			}
			if (MerchantsCardStateEnums.DISABLE.getIndex().equals(existing.getCardState())) {
				MerchantsCardEntity restoreEntity = new MerchantsCardEntity();
				restoreEntity.setId(existing.getId());
				restoreEntity.setCardState(MerchantsCardStateEnums.NORMAL.getIndex());
				merchantsCardDao.updateById(restoreEntity);
			}
		}
		if (!toAddIds.isEmpty()) {
			addMerchantsCard(toAddIds, merchantsInfoEntity, channelInfoEntity);
		}

		if (CollectionUtils.isEmpty(existingCards)) {
			return;
		}
		for (MerchantsCardEntity existing : existingCards) {
			Integer channelCardId = existing.getChannelCardId();
			if (channelCardId == null || incomingSet.contains(channelCardId)) {
				continue;
			}
			if (!MerchantsCardStateEnums.NORMAL.getIndex().equals(existing.getCardState())) {
				continue;
			}
			UpdateWrapper<MerchantsCardEntity> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", existing.getId())
					.set("card_state", MerchantsCardStateEnums.DISABLE.getIndex());
			merchantsCardDao.update(null, updateWrapper);
		}
	}

	/**
	 * 批量新增商户产品（优化了逐条查询和插入的性能问题）
	 */
	public void addMerchantsCard(List<Integer> channelCardsId,
	                             MerchantsInfoEntity merchantsInfoEntity,
	                             ChannelInfoEntity channelInfoEntity) throws InvocationTargetException, IllegalAccessException {
		// 1. 批量查询上游卡并校验
		List<ChannelCardEntity> channelCardList = channelCardDao.selectBatchIds(channelCardsId);
		if (channelCardList.size() != channelCardsId.size()) {
			throw new RuntimeException("部分上游卡不存在，请确认卡片信息");
		}

		// 2. 校验上游卡归属 & 过滤已存在的卡片
		for (ChannelCardEntity card : channelCardList) {
			if (!channelInfoEntity.getId().equals(card.getChannelId())) {
				throw new RuntimeException("上游卡与指定上游不匹配，cardId：" + card.getId());
			}

			MerchantsCardEntity entity = new MerchantsCardEntity();
			BeanUtils.copyProperties(card, entity);
			entity.setId(null);
			entity.setMchName(merchantsInfoEntity.getMerchantsNamme());
			entity.setChannelCardId(card.getId());
			entity.setMchId(merchantsInfoEntity.getId());
			entity.setMchAppid(merchantsInfoEntity.getAppId());
			entity.setChannelId(channelInfoEntity.getId());
			entity.setChannelCode(channelInfoEntity.getChannelCode());
			entity.setCardState(MerchantsCardStateEnums.NORMAL.getIndex());
			GenericityUtil.setDate(entity);
			merchantsCardDao.insert(entity);
		}
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
		FinanceRechargeRecordEntity entity = new FinanceRechargeRecordEntity();
		entity.setMerchantId(String.valueOf(merchantsFinanceQuery.getMchId()));
		entity.setFinanceAddress(merchantsFinanceQuery.getMerchantsAddress());
		entity.setAmount(merchantsFinanceQuery.getMerchantsAmount());
		entity.setTxStatus(TxStatusEnums.WAIT.getIndex().toString());
		entity.setRecordType(RecordTypeEnums.MANUAL.getIndex().toString());
		entity.setOperator(tokenUtils.getUsername());
		entity.setRemark(merchantsFinanceQuery.getRemark());
		GenericityUtil.setDate(entity);
		financeRechargeRecordDao.insert(entity);
		return setResultSuccess();
	}



}
