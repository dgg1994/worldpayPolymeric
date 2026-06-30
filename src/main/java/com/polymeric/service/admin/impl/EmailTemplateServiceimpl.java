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
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.heard.LanguageContext;
import com.polymeric.dao.template.EmailTemplateDao;
import com.polymeric.entity.template.EmailTemplateEntity;
import com.polymeric.enums.LanguageEnums;
import com.polymeric.service.admin.EmailTemplateService;
import com.polymeric.utils.EmailUtil;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.MessageFormatUtils;

@RestController
@Transactional
@CrossOrigin
public class EmailTemplateServiceimpl extends BaseApiService implements EmailTemplateService{
	
	@Autowired
	private EmailTemplateDao emailTemplateDao;

	@Override
	public ResponseBase add(@RequestBody EmailTemplateEntity entity) {
		try {
			//查询相同语言下code是否已存在
			EmailTemplateEntity template = emailTemplateDao.findByNum(entity.getTemplateNumber(),entity.getLanguage());
			if(template != null) {
				return setResultError(I18nUtil.getMessage("Template_exist"));
			}
			entity.setLanguageName(LanguageEnums.getLable(entity.getLanguage()));
			entity.setTemplateSubject(entity.getTemplateName());
			GenericityUtil.setDate(entity);
			emailTemplateDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(@RequestBody EmailTemplateEntity entity) {
		try {
			EmailTemplateEntity template = emailTemplateDao.selectById(entity.getId());
			if(template != null) {
				entity.setTemplateSubject(entity.getTemplateName());
				emailTemplateDao.updateById(entity);
				return setResultSuccess();
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase delete(Integer id) {
		try {
			EmailTemplateEntity template = emailTemplateDao.selectById(id);
			if(template != null) {
				emailTemplateDao.deleteById(id);
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
	public EmailTemplateEntity findCode(Integer code) {
		try {
			String language = LanguageContext.getLanguage();
			EmailTemplateEntity template = emailTemplateDao.findByNum(code, language);
			return template;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(@RequestBody EmailTemplateEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<EmailTemplateEntity> list = emailTemplateDao.findList(entity);
		PageInfo<EmailTemplateEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}

	@Async("asyncExecutor")
	public void send(String userEmail,Integer code, String language, Object... arg) {
		try {
			EmailTemplateEntity templateEntity = emailTemplateDao.findByNum(code,language);
			if(templateEntity != null) {
				//添加动态数据
				String htmlContent = MessageFormatUtils.format(
						templateEntity.getTemplateContent(),arg);
				String html = MessageFormatUtils.saveHtml(htmlContent, language);
				EmailUtil.sendEmail(userEmail, templateEntity.getTemplateSubject(), html);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
