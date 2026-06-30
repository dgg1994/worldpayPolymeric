package com.polymeric.utils.sign;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.query.sign.SignParams;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * RSA签名生成工具类
 * 与RsaVerifyUtil配套使用，用于生成签名
 * 
 * @author Hlin
 */
@Slf4j
public class RsaSignUtil {

    // 签名算法
    private static final String SIGN_ALGORITHM = "SHA256withRSA";
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 生成RSA签名
     * 
     * @param data 待签名数据（签名原文）
     * @param privateKeyStr 私钥（Base64编码，支持带或不带BEGIN/END标记）
     * @return Base64编码的签名，失败返回null
     */
    public static String sign(String data, String privateKeyStr) {
        if (StrUtil.isBlank(data) || StrUtil.isBlank(privateKeyStr)) {
            log.warn("签名参数为空");
            return null;
        }
        try {
            // 1. 清理私钥
            String cleanPrivateKey = cleanKey(privateKeyStr);

            // 2. 加载私钥
            byte[] keyBytes = Base64.getDecoder().decode(cleanPrivateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 3. 生成签名
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signBytes = signature.sign();
            String sign = Base64.getEncoder().encodeToString(signBytes);

            log.debug("生成签名成功，签名原文长度: {}, 签名: {}", data.length(), sign);
            return sign;

        } catch (Exception e) {
            log.error("RSA签名异常", e);
            return null;
        }
    }

    /**
     * 直接对请求参数生成签名
     * 
     * @param appId 应用ID
     * @param nonce 随机字符串
     * @param timestamp 时间戳
     * @param bodyJson 请求体JSON字符串
     * @param privateKeyStr 私钥
     * @return Base64编码的签名
     */
    public static String signRequest(String appId, String nonce, String timestamp, 
                                      String bodyJson, String privateKeyStr) {
        // 构建签名原文
        String signString = buildSignString(appId, nonce, timestamp, bodyJson);
        // 生成签名
        return sign(signString, privateKeyStr);
    }

    /**
     * 直接对Map参数生成签名
     * 
     * @param requestMap 包含所有参数的Map（包含appId、nonce、timestamp等）
     * @param privateKeyStr 私钥
     * @return Base64编码的签名
     */
    public static String signRequestFromMap(Map<String, Object> requestMap, String privateKeyStr) {
        String appId = (String) requestMap.get("appId");
        String nonce = (String) requestMap.get("nonce");
        String timestamp = (String) requestMap.get("timestamp");
        
        // 构建body（排除签名参数）
        Map<String, Object> bodyMap = new HashMap<>(requestMap);
        bodyMap.remove("appId");
        bodyMap.remove("nonce");
        bodyMap.remove("timestamp");
        bodyMap.remove("sign");
        String bodyJson = JSON.toJSONString(bodyMap);
        
        return signRequest(appId, nonce, timestamp, bodyJson, privateKeyStr);
    }

    /**
     * 构建签名原文
     * 
     */
    private static String buildSignString(String appId, String nonce, String timestamp, String bodyJson) {
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
                        sortedParams.put(key, getSortedJsonString((JSONObject) value));
                    } else if (value instanceof JSONArray) {
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

        return sb.toString();
    }

    /**
     * 递归排序JSON对象
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
                sortedMap.put(key, getSortedJsonArrayString((JSONArray) value));
            } else {
                sortedMap.put(key, value);
            }
        }

        return JSON.toJSONString(sortedMap);
    }

    /**
     * 排序JSONArray
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
                list.add(getSortedJsonString((JSONObject) item));
            } else if (item instanceof JSONArray) {
                list.add(getSortedJsonArrayString((JSONArray) item));
            } else {
                list.add(item);
            }
        }

        list.sort((a, b) -> {
            String sa = String.valueOf(a);
            String sb = String.valueOf(b);
            return sa.compareTo(sb);
        });

        sortedArray.addAll(list);
        return sortedArray.toJSONString();
    }

    /**
     * 清理密钥（移除BEGIN/END标记和空白字符）
     */
    private static String cleanKey(String keyStr) {
        if (keyStr == null) {
            return "";
        }
        return keyStr
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
    }

    /**
     * null转空字符串
     */
    private static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
    
    
    public static void main(String[] args) {
		String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC6Us3rB30HOz/MZxeumHaEzgsoNI080Z/RweaPi3IXcG/X1MKjz2qyMoB7+1scIhMXaKDObfsk6MH4JdYdXGeCCPunbFN9yKyl+gn7qb8ypQYy/Dl0OsLL3QpPncVNNnELBGBm1aMSuHoIrDkwx10s5/bYdm4EoiJjD7QPbEfiLTPR9bB/RtYrz8cwBoBaZ2Ek1Zb6VWQc8Kaugd1F/aZjr1WSUtctyR/je+TB3RjR2XEkJWUH5/tDH7oFWMYWM5kivRqNHULXsR8yM3qK3yRBApsGmSrGNgaeykQaGGWhQpzP9LHCPVAHjH1XJew/k+Zk8/2z0jI3vd4a27LdInIhAgMBAAECggEAL7NHOGZgYduPR7sU2wP5Q6KkRJS/m9RmwAKODi269bk5SN4VPm65eAlpz1PYTWmDzbhhgrexVg+oqOi22ilv7mByXLhk34zidlN+/sjERCjvX1onM1RfzmU2YI+ZlVzcr99c9ra22vITe+jrc0t21SpFjcE0BUek2guJI3GrLYHI+lak+op+9PoP1ZySDTxpJjTJI2h/BLHRbJh3QvY5ToHqu2p0QuVJmi5b4u0XAt628vOVLUaSy9RhfQvlFUzI9/q/hWJ5ljkPG3ExkmxzgWUwFmL3Di38WTbyQB7HQ2TeDfGC3nlbWnho0s3gi8fO2QDItVOJw0Ni+3VuNK5wAQKBgQDoT3PiDUqmnS3htS9GsdIqPFvrCWuKzwTzk1LLHZqf8vNNPxLV/qIJs7YoxbF8s/5nzlDiZMfKqlhl+pig6JDLlXDAQkr4t8w6+/v0YXjOr6UHS7LYH48EptXzk1X9aNxGGfWCOEz4n2JnHZ4P+eKokqx2Hxf0YfYE22HTjE4G4QKBgQDNUti6Eqkg4w4YJEKd4GPNjpNgUa+OdeokOScET+m/96iDh3HvGRis6fuIg+QsJ0oOltJH2ZnTfGFx/LEo6dyQ38mo3ozZuRNJRgzY1ffd6pWNNMPg7iqV1N8dLpUha5LHVWt5odwsQnsGw9mIFVlv2wM9Jn/Y94tkcyI0XNATQQKBgEVS+GzbtIw12WxCakI5bFMIQhHIdNwuXPbm3s0YH31dvbcufEt5uhcVFuXh4M4q1F9b1Q1KL8jNvqVn+8YAwLzP8p6c6cyA5KQxcAWEVM2w4pljwJH4EPC9bJlMPVvqv9j3hCkA7g8/WNkWoIV592aAppB1ROu7k/PtF6tOcjZhAoGBAME0lRW624VDww56iGkRLjjOUeXP19fBIztEwG8xV265d8uU12Q7BFoprvRNz5awveIuj97mZvZ30yY0tuwd69wziQorqPRzUC+iCBK+fQhIZsPCBBQTaOUms3A9Vj1lgW+q9IA3C/xl7mh2QKrqFAfKWijfomwiu/sgz+3f7DhBAoGBAK/2/HxZDmwnk28ncJiInUxxo8UAVwKDPcuJbAs4cIio60bf3ViCy01icA+46sGN8O9mycOiQeiTo0lP8aerTAU7pvZUDF3g8kfmjKclO4daLKqGkc9dlriQPjdTmZOluTYKH+A8NyQX6pg1XM0gJ2vUAUoUzd2275zWUTCsdLCT";
		String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAulLN6wd9Bzs/zGcXrph2hM4LKDSNPNGf0cHmj4tyF3Bv19TCo89qsjKAe/tbHCITF2igzm37JOjB+CXWHVxnggj7p2xTfcispfoJ+6m/MqUGMvw5dDrCy90KT53FTTZxCwRgZtWjErh6CKw5MMddLOf22HZuBKIiYw+0D2xH4i0z0fWwf0bWK8/HMAaAWmdhJNWW+lVkHPCmroHdRf2mY69VklLXLckf43vkwd0Y0dlxJCVlB+f7Qx+6BVjGFjOZIr0ajR1C17EfMjN6it8kQQKbBpkqxjYGnspEGhhloUKcz/Sxwj1QB4x9VyXsP5PmZPP9s9IyN73eGtuy3SJyIQIDAQAB";
		String appid = "MP_da74429a-c4d9-4d3f-9804-39f4debd9ea6";
		Map<String, Object> params = new HashMap<>();
		params.put("appId", appid);
		params.put("nonce", UUID.randomUUID().toString());
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("orderId", "ORD123");
		params.put("amount", 100);
		String sign = RsaSignUtil.signRequestFromMap(params, privateKey);
		System.out.println(sign);
		
		// 方法1：使用 extractSignParamsFromMap 提取参数并验证
	    SignParams signParams = RsaVerifyUtil.extractSignParamsFromMap(params);
//	    signParams.setSign(sign);  // 设置签名
	    
	    String signString = RsaVerifyUtil.buildSignString(
	        signParams.getAppId(),
	        signParams.getNonce(),
	        signParams.getTimestamp(),
	        signParams.getBody()
	    );
	    
	    boolean isValid = RsaVerifyUtil.verifySign(signString, sign, pubKey);
	    System.out.println("验签结果: " + (isValid ? "✓ 成功" : "✗ 失败"));
	}
    
    
}