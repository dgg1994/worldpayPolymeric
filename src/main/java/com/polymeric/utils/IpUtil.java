package com.polymeric.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;


@Component
public class IpUtil {

	/**
	 * 获取客户端真实 IP 地址（兼容代理）
	 */
	public String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For"); // 处理 Nginx 反向代理
		if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
			return ip.split(",")[0].trim(); // 获取真实 IP
		}
		ip = request.getHeader("Proxy-Client-IP");
		if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr(); // 直接获取 IP
	}

	/**
	 * 判断是否为本机或内网 IP
	 */
	public boolean isLocalIp(String ipAddress) {
		if (ipAddress == null || ipAddress.trim().isEmpty()) {
			return false;
		}
		ipAddress = ipAddress.trim();
		return "127.0.0.1".equals(ipAddress) || "::1".equals(ipAddress) // 本机
				|| ipAddress.startsWith("192.168.") // 私有内网
				|| ipAddress.startsWith("10.") // A 类内网
				|| (ipAddress.startsWith("172.") && Integer.parseInt(ipAddress.split("\\.")[1]) >= 16
						&& Integer.parseInt(ipAddress.split("\\.")[1]) <= 31); // B 类内网
	}

}
