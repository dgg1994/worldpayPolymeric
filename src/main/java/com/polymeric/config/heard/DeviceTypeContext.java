package com.polymeric.config.heard;

/**
 * @category 设备类型请求头上下文持有
 * @author Hlin
 *
 */
public class DeviceTypeContext {

	private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

	public static void setDeviceType(String deviceType) {
		CONTEXT.set(deviceType);
	}

	public static String getDeviceType() {
		return CONTEXT.get();
	}

	public static void clear() {
		CONTEXT.remove();
	}

}
