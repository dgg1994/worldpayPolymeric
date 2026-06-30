package com.polymeric.config.heard;

/**
 * @category 请求头版本号上下文持有
 * @author Hlin
 *
 */
public class AppVersionContext {

	private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

	public static void setAppVersion(String appVersion) {
		CONTEXT.set(appVersion);
	}

	public static String getAppVersion() {
		return CONTEXT.get();
	}

	public static void clear() {
		CONTEXT.remove();
	}

}
