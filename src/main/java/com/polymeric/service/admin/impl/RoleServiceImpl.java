package com.polymeric.service.admin.impl;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.aop.SysLogAnnotation;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.dao.system.SysRoleDao;
import com.polymeric.dao.system.SysRoleMenuDao;
import com.polymeric.entity.system.SysRoleEntity;
import com.polymeric.entity.system.SysRoleMenuEntity;
import com.polymeric.enums.RoleStateEnums;
import com.polymeric.enums.RoleTypeEnums;
import com.polymeric.service.admin.RoleService;
import com.polymeric.utils.I18nUtil;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@Transactional
public class RoleServiceImpl extends BaseApiService implements RoleService{
	
	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	

	@Override
	@SysLogAnnotation(module = "角色接口", type = "POST", remark = "查询所有角色")
	public ResponseBase findAll(@RequestBody SysRoleEntity entity) {
		try {
			entity.setDelFlag(RoleStateEnums.ROLE_DEL_FLAG_NORMAL.getIndex());
			PageHelper.startPage(entity.getPageNum(), entity.getPageSize());
			List<SysRoleEntity> list = sysRoleDao.findList(entity);
			PageInfo<SysRoleEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "GET", remark = "查询角色")
	public ResponseBase findById(Integer roleId) {
		SysRoleEntity entity = sysRoleDao.selectById(roleId);
		return setResultSuccess(entity,I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "POST", remark = "新增角色")
	public ResponseBase add(@RequestBody SysRoleEntity entity) {
		try {
			SysRoleEntity roleEntity = sysRoleDao.findName(entity.getRoleName(),RoleStateEnums.ROLE_DEL_FLAG_NORMAL.getIndex());
			if(roleEntity != null) {
				return setResultError(I18nUtil.getMessage("base_info_exist"));
			}else {
				entity.setCreateTime(new Date());
				sysRoleDao.insert(entity);
				if(entity.getMenuIds() != null && entity.getMenuIds().length > 0) {
					for (int i = 0; i < entity.getMenuIds().length; i++) {
						SysRoleMenuEntity roleMenuEntity = new SysRoleMenuEntity();
						roleMenuEntity.setCreateTime(new Date());
						roleMenuEntity.setRoleId(entity.getRoleId());
						roleMenuEntity.setMenuId(entity.getMenuIds()[i]);
						sysRoleMenuDao.insert(roleMenuEntity);
					}
				}
			}
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "POST", remark = "编辑角色信息")
	public ResponseBase update(@RequestBody SysRoleEntity entity) {
		try {
			SysRoleEntity roleEntity = sysRoleDao.selectById(entity.getRoleId());
			if(roleEntity != null) {
				SysRoleEntity role = sysRoleDao.findRoleIdName(entity.getRoleId(),entity.getRoleName(),RoleStateEnums.ROLE_DEL_FLAG_NORMAL.getIndex());
				if(role != null) {
					return setResultError(I18nUtil.getMessage("base_info_exist"));
				}
				entity.setUpdateTime(new Date());
				int temp = sysRoleDao.updateById(entity);
				if(temp > 0) {
					if(entity.getMenuIds() != null && entity.getMenuIds().length > 0) {
						sysRoleMenuDao.deleteRoleId(entity.getRoleId());
						for (int i = 0; i <entity.getMenuIds().length; i++) {
							SysRoleMenuEntity roleMenuEntity = new SysRoleMenuEntity();
							roleMenuEntity.setCreateTime(new Date());
							roleMenuEntity.setRoleId(entity.getRoleId());
							roleMenuEntity.setMenuId(entity.getMenuIds()[i]);
							sysRoleMenuDao.insert(roleMenuEntity);
						}
					}
				}
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_data_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "GET", remark = "删除角色")
	public ResponseBase delete(Integer roleId) {
		try {
			SysRoleEntity entity = sysRoleDao.selectById(roleId);
			if(entity != null) {
				entity.setDelFlag(RoleStateEnums.ROLE_DEL_FLAG_DELETE.getIndex());
				sysRoleDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_data_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "POST", remark = "角色停用启用")
	public ResponseBase changeStatus(Integer roleId, String status) {
		try {
			SysRoleEntity entity = sysRoleDao.selectById(roleId);
			if(entity != null) {
				entity.setStatus(status);
				sysRoleDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_data_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "GET", remark = "查询正常状态下角色信息")
	public ResponseBase findNormalRole() {
		List<SysRoleEntity> list = sysRoleDao.findNormalRole(RoleStateEnums.ROLE_DEL_FLAG_NORMAL.getIndex(),
				RoleStateEnums.ROLE_STATUS_NORMAL.getIndex(),RoleTypeEnums.ADMIN.getIndex());
		return setResultSuccess(JSON.toJSON(list),I18nUtil.getMessage("base_success"));
	}

}
