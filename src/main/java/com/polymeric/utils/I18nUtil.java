package com.polymeric.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.polymeric.config.heard.LanguageContext;

import java.util.Locale;

@Component
public class I18nUtil {

	private static MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		I18nUtil.messageSource = messageSource;
	}

	/**
	 * 获取国际化消息
	 * 
	 * @param code 配置文件中的 key
	 * @param args 占位符参数，可选
	 */
	public static String getMessage(String code, Object... args) {
		// 防御性判断：messageSource 是否为空
		if (messageSource == null) {
			return code;
		}
		
		String lang = LanguageContext.getLanguage();
		Locale locale;
		
		// 防御性判断：lang 为 null 时使用默认语言
		if (lang == null || lang.isEmpty()) {
			locale = Locale.ENGLISH;
		} else {
			switch (lang) {
				case "zh-hk": // 中文繁体
					locale = Locale.TAIWAN;
					break;
				case "zh-cn": // 中文简体
					locale = Locale.SIMPLIFIED_CHINESE;
					break;
				case "tr-tr": // 土耳其语
					locale = new Locale("tr", "TR");
					break;
				case "ko-kr": // 韩语
					locale = new Locale("ko", "KR");
					break;
				case "ja-jp": // 日语
					locale = new Locale("ja", "JP");
					break;
				case "pt-br": // 巴西葡萄牙语
					locale = new Locale("pt", "BR");
					break;
				case "en": // 英文
					locale = Locale.ENGLISH;
					break;
				default:
					locale = Locale.ENGLISH;
			}
		}
		
		try {
			return messageSource.getMessage(code, args, locale);
		} catch (Exception e) {
			// 如果获取失败，返回 code 本身
			return code;
		}
	}
}