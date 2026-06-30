package com.polymeric.config.heard;

/**
 * @category 语言请求头上下文持有
 * @author Hlin
 *
 */
public class LanguageContext {

	private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

	public static void setLanguage(String language) {
		CONTEXT.set(language);
	}

	public static String getLanguage() {
		return CONTEXT.get();
	}

	public static void clear() {
		CONTEXT.remove();
	}

}
