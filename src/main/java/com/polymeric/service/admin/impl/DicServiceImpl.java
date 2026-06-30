package com.polymeric.service.admin.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.polymeric.aop.SysLogAnnotation;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.entity.system.DicEntity;
import com.polymeric.enums.DeviceTypeEnums;
import com.polymeric.enums.LanguageEnums;
import com.polymeric.enums.OrderStatusEnum;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.service.admin.DicService;
import com.polymeric.utils.I18nUtil;

@RestController
@Transactional
@CrossOrigin
public class DicServiceImpl extends BaseApiService implements DicService{
		
	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询语言列表")
	public ResponseBase getLanguage() {
		List<DicEntity> list = LanguageEnums.getLableList();
		return setResultSuccess(list,I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询用户状态")
	public ResponseBase findUserState() {
		List<DicEntity> list = UserStateEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}
	
	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询设备类型")
	public ResponseBase findDeviceType() {
		List<DicEntity> list = DeviceTypeEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase findOrderState() {
		List<DicEntity> list = OrderStatusEnum.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}




}
