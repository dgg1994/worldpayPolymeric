package com.polymeric.config;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @category 多语言配置
 * @author Hlin
 *
 */
@Configuration
public class I18nConfig {
	
	@Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages"); // 不带语言后缀
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
