package com.polymeric.service.admin.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
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
import com.polymeric.dao.merchants.MerchantsKeyDao;
import com.polymeric.dao.system.SysRoleDao;
import com.polymeric.dao.system.SysUserDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsIpEntity;
import com.polymeric.entity.merchants.MerchantsKeyEntity;
import com.polymeric.entity.system.SysRoleEntity;
import com.polymeric.entity.system.SysUserEntity;
import com.polymeric.enums.RoleTypeEnums;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.response.pub.PublicRes;
import com.polymeric.response.sign.KeyPairResult;
import com.polymeric.service.admin.MerchantsService;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.GoogleAuthenticatorUtil;
import com.polymeric.utils.sign.KeyPairUtil;


@RestController
@Transactional
@CrossOrigin
public class MerchantsServiceImpl extends BaseApiService implements MerchantsService{
	
	@Autowired
	private MerchantsInfoDao merchantsInfoDao;
	
	@Autowired
	private MerchantsKeyDao merchantsKeyDao;
	
	@Autowired
	private MerchantsIpDao merchantsIpDao;
	
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
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<MerchantsInfoEntity> list = merchantsInfoDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					List<MerchantsIpEntity> ipEntities = merchantsIpDao.findByMerchantsId(list.get(i).getId());
					list.get(i).setIpList(ipEntities);
					MerchantsKeyEntity keyEntity = merchantsKeyDao.findAppId(list.get(i).getAppId());
					list.get(i).setMerchantsKey(keyEntity);
					
				}
			}
			PageInfo<MerchantsInfoEntity> info = new PageInfo<>(list);
			MerchantsInfoEntity sum = merchantsInfoDao.findSum(entity);
			PublicRes<MerchantsInfoEntity> res = new PublicRes<>();
			res.setData(sum);
			res.setInfo(info);
			return setResultSuccess(res, Constants.SUCCESS);
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
	
	


	
}
