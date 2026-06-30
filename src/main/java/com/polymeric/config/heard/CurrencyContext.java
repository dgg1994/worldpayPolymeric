package com.polymeric.config.heard;

/**
 * @category 货币类型请求头上下文持有
 * @author Hlin
 *
 */
public class CurrencyContext {
	
	private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

	public static void setCurrency(String currency) {
		CONTEXT.set(currency);
	}

	public static String getCurrency() {
		return CONTEXT.get();
	}

	public static void clear() {
		CONTEXT.remove();
	}

}
