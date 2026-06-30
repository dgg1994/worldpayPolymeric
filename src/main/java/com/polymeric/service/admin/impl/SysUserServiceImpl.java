package com.polymeric.service.admin.impl;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.aop.SysLogAnnotation;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.system.SysUserDao;
import com.polymeric.entity.system.SysUserEntity;
import com.polymeric.entity.system.SysUserPowerEntity;
import com.polymeric.enums.RoleTypeEnums;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.filter.JWTAuthenticationFilter;
import com.polymeric.service.admin.SysUserService;
import com.polymeric.utils.GoogleAuthenticatorUtil;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.RedisUtil;

@RestController
@Transactional
@CrossOrigin
public class SysUserServiceImpl extends BaseApiService implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	RedisUtil redisUtil;

	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "用户列表分页查询")
	public ResponseBase findList(@RequestBody SysUserEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		if (entity.getRoleId() != null && entity.getRoleId().size() < 1) {
			entity.setRoleId(null);
		}
		List<SysUserEntity> list = sysUserDao.findNotDeleteAll(entity);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setUserStateNmae(UserStateEnums.getValue(list.get(i).getUserState()));
				list.get(i).setRoleList(sysUserDao.findUserRole(list.get(i).getId()));
			}
		}
		PageInfo<SysUserEntity> pageInfo = new PageInfo<>(list);
		return setResultSuccess(pageInfo,I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "用户注册")
	public ResponseBase signUp(String user, MultipartFile file) {
		try {
			SysUserEntity entity = JSON.parseObject(user, SysUserEntity.class);
			List<SysUserEntity> userEntity = sysUserDao.findByAcctiveAndTel(entity.getAcctive(), entity.getTel());//,UserStateEnums.NORMAL.getIndex()
			if (userEntity != null && userEntity.size() > 0) {
				return setResultError(I18nUtil.getMessage("base_info_exist"));
			} else {
				entity.setUserState(UserStateEnums.NORMAL.getIndex());
				if (entity.getPassword() != null && entity.getPassword().length() > 0) {
					entity.setPassword(DigestUtils.md5DigestAsHex((entity.getPassword()).getBytes()));
				} 
				if(entity.getGoogleSecretkey() == null || entity.getGoogleSecretkey().isEmpty()) {
					entity.setGoogleSecretkey(Constants.user_googleKey);
				}
				sysUserDao.insert(entity);
				if (entity.getRoleId() != null && entity.getRoleId().size() > 0) {
					for (int i = 0; i < entity.getRoleId().size(); i++) {
						sysUserDao.addUserOrRole(entity.getId(), entity.getRoleId().get(i));
					}
				}
				return setResultSuccess(JSON.toJSON(entity), I18nUtil.getMessage("base_success"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户管理", type = "GET", remark = "通过token获取用户信息")
	public ResponseBase findToken(HttpServletRequest request) {
		String tokenStr = request.getHeader(Constants.HEADER_AUTH);
		UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
		String username = token.getName();
		Collection<GrantedAuthority> powerList = token.getAuthorities();
		SysUserEntity entity = sysUserDao.findByAcctiveState(username, UserStateEnums.NORMAL.getIndex());
		redisUtil.set(tokenStr, JSON.toJSONString(entity), Constants.REDIS_EXPIRE_TIME);
		entity.setRoleList(sysUserDao.findUserRole(entity.getId()));
		List<SysUserPowerEntity> newPowerList = new ArrayList<>();
		for (GrantedAuthority grantedAuthority : powerList) {
			SysUserPowerEntity powerEntity = new SysUserPowerEntity();
			powerEntity.setAuthority(grantedAuthority.getAuthority());
			newPowerList.add(powerEntity);
		}
		entity.setPower(newPowerList);
		boolean temp = false;
		if (entity.getRoleList() != null && entity.getRoleList().size() > 0) {
			for (int i = 0; i < entity.getRoleList().size(); i++) {
				if (RoleTypeEnums.ADMIN.getIndex().equals(entity.getRoleList().get(i).getRoleId())) {// 管理员
					temp = true;
				}
			}
		}
		if (temp) {
			List<String> roles = new ArrayList<String>();
			roles.add(Constants.ADMIN_STR);
			entity.setRoles(roles);
			List<String> perms = new ArrayList<String>();
			perms.add("*:*:*");
			entity.setPermissions(perms);
		} else {
			entity.setRoles(sysUserDao.findRoleKey(entity.getId()));
			entity.setPermissions(sysUserDao.findPermissions(entity.getId()));
		}

		entity.setLoginTime(new Date());
		sysUserDao.updateById(entity);
		return setResultSuccess(JSON.toJSON(entity),I18nUtil.getMessage("base_success"));
	}


	@Override
	@SysLogAnnotation(module = "用户管理", type = "GET", remark = "删除用户信息")
	public ResponseBase delUser(@PathVariable("userId") Integer userId) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null) {
				sysUserDao.updateState(userId, UserStateEnums.LOGOUT.getIndex());
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "改变用户账号状态")
	public ResponseBase updateUserState(Integer userId, Integer newState) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null) {
				entity.setUserState(newState);
				sysUserDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "编辑用户信息")
	public ResponseBase updateUser(String user, MultipartFile file) {
		try {
			SysUserEntity entity = JSON.parseObject(user, SysUserEntity.class);
			SysUserEntity userEntity = sysUserDao.findById(entity.getId());
			if (userEntity != null) {
				List<SysUserEntity> userSysUserEntity = sysUserDao.findByNoAcctiveAndTel(entity.getAcctive(),
						entity.getTel(), entity.getId(), UserStateEnums.NORMAL.getIndex());
				if (userSysUserEntity != null && userSysUserEntity.size() > 0) {
					return setResultError(I18nUtil.getMessage("base_info_exist"));
				}
				sysUserDao.updateById(entity);
				if (entity.getRoleId() != null && entity.getRoleId().size() > 0) {
					sysUserDao.delteRole(userEntity.getId());
					for (int i = 0; i < entity.getRoleId().size(); i++) {
						sysUserDao.addUserOrRole(userEntity.getId(), entity.getRoleId().get(i));
					}
				}
				SysUserEntity result = sysUserDao.findById(entity.getId());
				return setResultSuccess(result,I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}



	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "重置密码")
	public ResponseBase resetUserPwd(Integer userId,String password) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null) {
				String pwd = DigestUtils.md5DigestAsHex((password).getBytes());
				sysUserDao.resetUserPwd(userId, pwd);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "修改密码")
	public ResponseBase resetPwd(@RequestParam("userId") Integer userId, @RequestParam("password") String password) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null) {
				String pwd = DigestUtils.md5DigestAsHex((password).getBytes());
				sysUserDao.resetUserPwd(userId, pwd);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "校验账号密码")
	public ResponseBase verifyPwd(Integer userId, String password) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null && entity.getPassword().equals(DigestUtils.md5DigestAsHex((password).getBytes()))) {
				return setResultSuccess(1, I18nUtil.getMessage("check_success"));
			} else {
				return setResultError(0, I18nUtil.getMessage("old_password_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}



	@Override
	public ResponseBase updateDeviceModel(@RequestBody SysUserEntity entity) {
		if (entity.getId() != null) {
			SysUserEntity e = sysUserDao.findById(entity.getId());
			if (e != null) {
				sysUserDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			} else {
				return setResultError(I18nUtil.getMessage("base_data_null"));
			}
		}else {
			return setResultError(I18nUtil.getMessage("base_data_null"));
		}
	}

	

	@Override
	public ResponseBase upGoogleSecretkey(Integer userId,String googleSecretkey) {
		try {
			SysUserEntity userEntity = sysUserDao.selectById(userId);
			if(userEntity != null) {
				userEntity.setGoogleSecretkey(googleSecretkey);
				sysUserDao.updateById(userEntity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase IssueGoogleSecretkey(String userName) {
		try {
			String key = GoogleAuthenticatorUtil.createKey(userName).getKey();
			return setResultSuccess(key, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase getPasswordOrKey() {
		try {
			String password = GoogleAuthenticatorUtil.generateDefaultPassword();
			String key = GoogleAuthenticatorUtil.createKey(password).getKey();
			Map<String, Object> map = new HashMap<>();
			map.put("password", password);
			map.put("key", key);
			return setResultSuccess(map,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}



}
