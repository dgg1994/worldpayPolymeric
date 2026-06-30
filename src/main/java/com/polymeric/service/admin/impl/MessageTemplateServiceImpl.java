package com.polymeric.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.aop.SysLogAnnotation;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.heard.LanguageContext;
import com.polymeric.dao.template.EmailTemplateDao;
import com.polymeric.dao.template.MessageTemplateDao;
import com.polymeric.entity.template.EmailTemplateEntity;
import com.polymeric.entity.template.MessageTemplateEntity;
import com.polymeric.enums.LanguageEnums;
import com.polymeric.service.admin.MessageTemplateService;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.MessageFormatUtils;
import com.polymeric.utils.TelegramNotificationUtil;

@RestController
@Transactional
@CrossOrigin
public class MessageTemplateServiceImpl extends BaseApiService implements MessageTemplateService{
	
	@Autowired
	private MessageTemplateDao messageTemplateDao;
	
	@Autowired
	private EmailTemplateDao emailTemplateDao;
	
	
	@Autowired
	private TelegramNotificationUtil telegramNotificationUtil;

	@Override
	@SysLogAnnotation(module = "消息模板", type = "post", remark = "新增消息模板")
	public ResponseBase add(@RequestBody MessageTemplateEntity entity) {
		try {
			//查询相同语言下code是否已存在
			MessageTemplateEntity template = messageTemplateDao.findOne(entity.getMessageCode(),entity.getLanguage());
			if(template != null) {
				return setResultError(I18nUtil.getMessage("language_inf_exit"));
			}
			entity.setLanguageName(LanguageEnums.getLable(entity.getLanguage()));
			GenericityUtil.setDate(entity);
			messageTemplateDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "消息模板", type = "post", remark = "编辑消息模板")
	public ResponseBase update(@RequestBody MessageTemplateEntity entity) {
		try {
			MessageTemplateEntity template = messageTemplateDao.selectById(entity.getId());
			if(template != null) {
				messageTemplateDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "消息模板", type = "get", remark = "删除消息模板")
	public ResponseBase delete(Integer id) {
		try {
			MessageTemplateEntity template = messageTemplateDao.selectById(id);
			if(template != null) {
				messageTemplateDao.deleteById(id);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "消息模板", type = "get", remark = "根据code查询消息模板")
	public MessageTemplateEntity findCode(Integer code) {
		try {
			String language = LanguageContext.getLanguage();
			MessageTemplateEntity template = messageTemplateDao.findOne(code, language);
			return template;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	

	@Override
	@SysLogAnnotation(module = "消息模板", type = "post", remark = "根据code查询消息模板")
	public ResponseBase findList(@RequestBody MessageTemplateEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<MessageTemplateEntity> list = messageTemplateDao.findList(entity);
		PageInfo<MessageTemplateEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}
	
	
	/**
	 * @category 发送机器人飞机消息
	 * @param templateCode
	 * @param language
	 * @param uid
	 * @param params
	 */
	@Async("asyncExecutor")
	public void sendTelegramAsync(Integer templateCode, String language, Object... params) {
		EmailTemplateEntity templateEntity = emailTemplateDao.findByNum(templateCode,language);
		if(templateEntity != null) {
			//添加动态数据
			String htmlContent = MessageFormatUtils.format(
					templateEntity.getTemplateContent(),params);
			String html = MessageFormatUtils.saveTelegramHtml(htmlContent);
			telegramNotificationUtil.sendTelegramBindingMsg(html);
		}
	}
	
	
}
