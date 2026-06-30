package com.polymeric.utils.sign;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.query.sign.SignParams;
import com.polymeric.utils.RedisUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RsaVerifyUtil {

	private static RedisUtil redisUtil;

	@Autowired
	public void setRedisUtil(RedisUtil redisUtil) {
		RsaVerifyUtil.redisUtil = redisUtil;
	}

	// 签名算法
	private static final String SIGN_ALGORITHM = "SHA256withRSA";

	private static final String KEY_ALGORITHM = "RSA";

	// 时间戳有效时间（秒）
	private static final int TIMESTAMP_TIMEOUT = 600;

	// Nonce缓存时间（秒）
	private static final int NONCE_TIMEOUT = 600;

	/**
	 * 从HttpServletRequest中提取签名参数
	 */
	public static SignParams extractSignParams(HttpServletRequest request) {
		SignParams params = new SignParams();

		// 从Header获取签名参数
		params.setAppId(request.getHeader("appId"));
		params.setNonce(request.getHeader("nonce"));
		params.setTimestamp(request.getHeader("timestamp"));
		params.setSign(request.getHeader("sign"));

		// 读取请求体
		try {
			StringBuilder sb = new StringBuilder();
			try (BufferedReader reader = request.getReader()) {
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			}
			params.setBody(sb.toString());
		} catch (Exception e) {
			log.error("读取请求体失败", e);
		}

		return params;
	}

	/**
	 * 从Map中提取签名参数（非Servlet环境使用）
	 */
	public static SignParams extractSignParamsFromMap(Map<String, Object> requestMap) {
		SignParams params = new SignParams();

		params.setAppId((String) requestMap.get("appId"));
		params.setNonce((String) requestMap.get("nonce"));
		params.setTimestamp((String) requestMap.get("timestamp"));
		params.setSign((String) requestMap.get("sign"));

		// body就是去掉签名参数后的其他参数
		Map<String, Object> bodyMap = new HashMap<>(requestMap);
		bodyMap.remove("appId");
		bodyMap.remove("nonce");
		bodyMap.remove("timestamp");
		bodyMap.remove("sign");
		params.setBody(JSON.toJSONString(bodyMap));

		return params;
	}

	/**
	 * 构建签名原文
	 */
	public static String buildSignString(String appId, String nonce, String timestamp, String bodyJson) {
		StringBuilder sb = new StringBuilder();
		sb.append("appId=").append(nullToEmpty(appId));
		sb.append("&nonce=").append(nullToEmpty(nonce));
		sb.append("&timestamp=").append(nullToEmpty(timestamp));

		if (bodyJson != null && StrUtil.isNotBlank(bodyJson) && !"null".equalsIgnoreCase(bodyJson.trim())) {
			try {
				JSONObject bodyObject = JSON.parseObject(bodyJson);
				TreeMap<String, Object> sortedParams = new TreeMap<>();

				for (Map.Entry<String, Object> entry : bodyObject.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();

					if (value == null) {
						continue;
					}

					if (value instanceof JSONObject) {
						// 🔧 使用排序后的字符串
						sortedParams.put(key, getSortedJsonString((JSONObject) value));
					} else if (value instanceof JSONArray) {
						// 🔧 修复：数组也需要排序
						sortedParams.put(key, getSortedJsonArrayString((JSONArray) value));
					} else {
						sortedParams.put(key, value);
					}
				}

				for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
					sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
				}
			} catch (Exception e) {
				log.error("解析Body参数失败", e);
			}
		}

		String result = sb.toString();
		log.debug("签名原文: {}", result);
		return result;
	}

	/**
	 * 递归排序JSON对象（修复版）
	 */
	private static String getSortedJsonString(JSONObject jsonObject) {
		if (jsonObject == null || jsonObject.isEmpty()) {
			return "{}";
		}

		TreeMap<String, Object> sortedMap = new TreeMap<>();
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value == null) {
				continue;
			}

			if (value instanceof JSONObject) {
				sortedMap.put(key, getSortedJsonString((JSONObject) value));
			} else if (value instanceof JSONArray) {
				// 🔧 修复：对数组进行排序处理
				sortedMap.put(key, getSortedJsonArrayString((JSONArray) value));
			} else {
				sortedMap.put(key, value);
			}
		}

		return JSON.toJSONString(sortedMap);
	}

	/**
	 * 排序JSONArray（新增方法）
	 */
	private static String getSortedJsonArrayString(JSONArray jsonArray) {
		if (jsonArray == null || jsonArray.isEmpty()) {
			return "[]";
		}

		JSONArray sortedArray = new JSONArray();
		List<Object> list = new ArrayList<>();

		for (int i = 0; i < jsonArray.size(); i++) {
			Object item = jsonArray.get(i);
			if (item instanceof JSONObject) {
				// 递归排序对象
				list.add(getSortedJsonString((JSONObject) item));
			} else if (item instanceof JSONArray) {
				// 递归排序数组
				list.add(getSortedJsonArrayString((JSONArray) item));
			} else {
				list.add(item);
			}
		}

		// 排序数组元素
		list.sort((a, b) -> {
			String sa = String.valueOf(a);
			String sb = String.valueOf(b);
			return sa.compareTo(sb);
		});

		sortedArray.addAll(list);
		return sortedArray.toJSONString();
	}

	/**
	 * RSA验签
	 * @param data 构建签名
	 * @param sign 传的签名
	 * @param publicKeyStr 公钥
	 * @return
	 */
	public static boolean verifySign(String data, String sign, String publicKeyStr) {
		if (StrUtil.isBlank(data) || StrUtil.isBlank(sign) || StrUtil.isBlank(publicKeyStr)) {
			log.warn("验签参数为空");
			return false;
		}
		try {
			// 1. 清理公钥
			String cleanPublicKey = publicKeyStr
					.replace("-----BEGIN PUBLIC KEY-----", "")
					.replace("-----END PUBLIC KEY-----", "")
					.replace("-----BEGIN RSA PUBLIC KEY-----", "")
					.replace("-----END RSA PUBLIC KEY-----", "")
					.replaceAll("\\s", "");

			// 2. 加载公钥
			byte[] keyBytes = Base64.getDecoder().decode(cleanPublicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance(SIGN_ALGORITHM);
			signature.initVerify(publicKey);
			signature.update(data.getBytes(StandardCharsets.UTF_8));
			byte[] signBytes = Base64.getDecoder().decode(sign);
			boolean isValid = signature.verify(signBytes);

			if (!isValid) {
				log.warn("签名验证失败，签名原文: {}", data);
			}

			return isValid;

		} catch (Exception e) {
			log.error("RSA验签异常", e);
			return false;
		}
	}

	/**
	 * 验证时间戳
	 */
	public static boolean validateTimestamp(String timestamp, int timeoutSeconds) {
		if (StrUtil.isBlank(timestamp)) {
			log.warn("时间戳为空");
			return false;
		}
		try {
			long ts = Long.parseLong(timestamp);
			long currentTime = System.currentTimeMillis();
			long diff = Math.abs(currentTime - ts);

			if (diff > timeoutSeconds * 1000L) {
				log.warn("时间戳过期: timestamp={}, current={}, diff={}ms", ts, currentTime, diff);
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			log.warn("时间戳格式错误: {}", timestamp);
			return false;
		}
	}

	/**
	 * 验证时间戳（使用默认超时时间）
	 */
	public static boolean validateTimestamp(String timestamp) {
		return validateTimestamp(timestamp, TIMESTAMP_TIMEOUT);
	}

	/**
	 * 验证随机数是否重复使用
	 * 
	 * @param appId 应用ID
	 * @param nonce 随机数
	 * @return true=有效（未使用过），false=无效（已使用过或Redis未初始化）
	 */
	public static boolean validateNonce(String appId, String nonce) {
		if (StrUtil.isBlank(appId) || StrUtil.isBlank(nonce)) {
			log.warn("appId或nonce为空");
			return false;
		}

		if (redisUtil == null) {
			log.error("RedisUtil未初始化，无法验证nonce");
			return false;
		}
		String key = "api:nonce:" + appId + ":" + nonce;
		try {
			// 检查nonce是否已经使用过
			if (redisUtil.hasKey(key)) {
				log.warn("Nonce已被使用: appId={}, nonce={}", appId, nonce);
				return false;
			}
			// 存储nonce，设置过期时间为10分钟
			boolean success = redisUtil.set(key, "1", NONCE_TIMEOUT);
			if (success) {
				log.debug("Nonce验证成功: appId={}, nonce={}", appId, nonce);
			}
			return success;
		} catch (Exception e) {
			log.error("验证nonce异常", e);
			return false;
		}
	}

	/**
	 * null转空字符串
	 */
	private static String nullToEmpty(String str) {
		return str == null ? "" : str;
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
	
}
