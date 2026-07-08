package com.polymeric.service.admin.impl;

import com.polymeric.aop.SysLogAnnotation;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.entity.system.DicEntity;
import com.polymeric.enums.*;
import com.polymeric.service.admin.DicService;
import com.polymeric.utils.I18nUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询用户KYC状态")
	public ResponseBase findKycState() {
		List<DicEntity> list = KycStateEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询设备类型")
	public ResponseBase findDeviceType() {
		List<DicEntity> list = DeviceTypeEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询订单状态")
	public ResponseBase findOrderState() {
		List<DicEntity> list = OrderStatusEnum.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询订单类型")
	public ResponseBase findOrderTradeType() {
		List<DicEntity> list = OrderTradeTypeEnum.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询交易类型")
	public ResponseBase findTradeType() {
		List<DicEntity> list = TradeTypeEnum.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询订单类型")
	public ResponseBase findOrderType() {
		List<DicEntity> list = OrderTypeEnum.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询回调类型")
	public ResponseBase findWebhookPoloType() {
		List<DicEntity> list = WebhookPoloTypeEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询银行卡状态")
	public ResponseBase findCardState() {
		List<DicEntity> list = CardStateEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}


}
