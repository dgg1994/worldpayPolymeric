package com.polymeric.utils.sign;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.response.pub.ApiResponseEntity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

@Slf4j
@Component
public class ApiPoplCardApi extends BaseApiService {

    private static String GATEWAY;
    private static String APP_ID;
    private static String RSA_PRIVATE_KEY;

	public static final int NOTIFY_TIMEOUT = 30000;//响应超时时间

	public static final int NOTIFY_CONNECT_TIMEOUT = 5000;//连接超时时间

    public static boolean useProxy = false;
    public static String proxyAddress = "127.0.0.1";
    public static int proxyPort = 7070;
    

    @Value("${bank.gatewayUrl}")
    public void setGateway(String gateway) {
        ApiPoplCardApi.GATEWAY = gateway;
    }

    @Value("${bank.appId}")
    public void setAppId(String appId) {
        ApiPoplCardApi.APP_ID = appId;
    }

    @Value("${bank.rsaPrivateKey}")
    public void setRsaPrivateKey(String rsaPrivateKey) {
        ApiPoplCardApi.RSA_PRIVATE_KEY = rsaPrivateKey;
    }


    /**
     * 生成32位随机数
     */
    private static String generateNonce() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * RSA签名（支持多种算法）
     */
    private static String rsaSign(String data, String privateKeyStr, String algorithm) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        
        byte[] signed = signature.sign();
        
        // 标准Base64
        String standardSign = Base64.encodeBase64String(signed);
      
        // 默认返回标准Base64，如果服务器要求URL安全的，可以改为返回urlSafeSign
        return standardSign;
    }

    /**
     * RSA签名（默认使用SHA1withRSA，因为很多支付平台用这个）
     */
    private static String rsaSign(String data, String privateKeyStr) {
        try {
            // 先尝试SHA1withRSA（支付平台常用）
            return rsaSign(data, privateKeyStr, "SHA1withRSA");
        } catch (Exception e) {
            try {
                return rsaSign(data, privateKeyStr, "SHA256withRSA");
            } catch (Exception ex) {
                throw new RuntimeException("RSA签名失败", ex);
            }
        }
    }

    /**
     * 递归排序JSON对象
     */
    private static String getChildSortedString(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.isEmpty()) {
            return "";
        }

        TreeMap<String, Object> childMap = new TreeMap<>();

        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof JSONObject) {
                childMap.put(key, getChildSortedString((JSONObject) value));
            } else {
                childMap.put(key, value);
            }
        }

        StringBuilder sb = new StringBuilder("{");
        Iterator<Map.Entry<String, Object>> it = childMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            sb.append(entry.getKey())
              .append(":")
              .append(entry.getValue());

            if (it.hasNext()) {
                sb.append(",");
            }
        }

        sb.append("}");
        return sb.toString().replace("\"", "");
    }

    /**
     * 构建签名字符串
     * 规则：header固定顺序 appId -> nonce -> timestamp，每个参数之间用&连接
     *      然后追加body参数，每个body参数前也加&，body参数内部按ASCII排序
     */
    private static String buildSignString(String appId, String nonce, String timestamp, Object bodyObject) {
        StringBuilder sb = new StringBuilder();
        
        // 1. header固定顺序：appId -> nonce -> timestamp（用&连接）
        sb.append("appId=").append(appId);
        sb.append("&nonce=").append(nonce);
        sb.append("&timestamp=").append(timestamp);
        
        // 2. body参数处理
        if (bodyObject != null) {
            JSONObject bodyJson = JSON.parseObject(JSON.toJSONString(bodyObject));
            
            // 对body参数按ASCII排序
            TreeMap<String, Object> bodyMap = new TreeMap<>();
            
            for (Map.Entry<String, Object> entry : bodyJson.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                
                if (value == null || value.toString().trim().isEmpty()) {
                    continue;
                }
                
                if (value instanceof JSONObject) {
                    bodyMap.put(key, getChildSortedString((JSONObject) value));
                } else {
                    bodyMap.put(key, value);
                }
            }
            
            // 3. 追加body参数
            // 第一个body参数前不加&，后面的加&
            boolean isFirstBodyParam = true;
            for (Map.Entry<String, Object> entry : bodyMap.entrySet()) {
                if (isFirstBodyParam) {
                    sb.append(entry.getKey())
                      .append("=")
                      .append(entry.getValue());
                    isFirstBodyParam = false;
                } else {
                    sb.append("&")
                      .append(entry.getKey())
                      .append("=")
                      .append(entry.getValue());
                }
            }
        }
        
        String result = sb.toString().replace("\"", "");
        log.info("签名原文: {}", result);
        return result;
    }

    /**
     * POST请求
     */
    public static ResponseBase postData(String uId, String method, Object object, String requestOrderId) {
        try {
        	
            String url = GATEWAY + method;
            log.info("请求地址：{}", url);
            String nonce = generateNonce();
            String timestamp = String.valueOf(System.currentTimeMillis());
            // 1. 构建签名字符串
            String signStr = buildSignString(APP_ID, nonce, timestamp, object);
            // 2. RSA签名
            String sign = rsaSign(signStr, RSA_PRIVATE_KEY);
            // 3. 构建HTTP请求
            HttpRequest httpRequest = HttpRequest.post(url)
                    .header("appId", APP_ID)
                    .header("nonce", nonce)
                    .header("timestamp", timestamp)
                    .header("sign", sign)
            		.header("version", "v1");

            if (!Strings.isNullOrEmpty(uId)) {
            	 httpRequest.header("uId", uId);
            }
            if (!Strings.isNullOrEmpty(requestOrderId)) {
                httpRequest.header("requestOrderId", requestOrderId);
            }

            if (useProxy) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(proxyAddress, proxyPort));
                httpRequest.setProxy(proxy);
            }

            // 4. 发送请求
            String dataStr = httpRequest
                    .timeout(NOTIFY_TIMEOUT)
                    .body(JSON.toJSONString(object))
                    .charset(StandardCharsets.UTF_8)
                    .setConnectionTimeout(NOTIFY_CONNECT_TIMEOUT)
                    .execute()
                    .body();
            log.info("响应原始数据: {}", dataStr);
            // 5. 解析响应
            ApiResponseEntity responseEntity =
                    JSONObject.parseObject(dataStr, ApiResponseEntity.class);
            if (Constants.ZERO_INT == responseEntity.getCode()) {
                return setResultSuccess(responseEntity.getData(), responseEntity.getMsg());
            } else {
                return setResult(responseEntity.getCode(),
                        responseEntity.getMsg(), null);
            }

        } catch (Exception e) {
            log.error("请求异常", e);
            return setResult(Constants.HTTP_RES_CODE_500,
                    "系统异常：" + e.getMessage(), null);
        }
    }

    /**
     * 文件上传方法
     */
    public static ResponseBase postFormFile(String uId, String filedName, File file, String method) throws IOException {
        try {
            String url = GATEWAY + method;
            // 1. 生成随机数和时间戳
            String nonce = generateNonce();
            String timestamp = String.valueOf(System.currentTimeMillis());
            
            // 2. 构建签名参数（只包含需要签名的字段）
            TreeMap<String, Object> signMap = new TreeMap<>();
            signMap.put("appId", APP_ID);
            signMap.put("nonce", nonce);
            signMap.put("timestamp", timestamp);
            // 注意：uid 不参与签名，只放在header中
            
            // 3. 构建签名字符串（ASCII排序）
            StringBuilder signStrBuilder = new StringBuilder();
            Iterator<Map.Entry<String, Object>> it = signMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                signStrBuilder.append(entry.getKey())
                             .append("=")
                             .append(entry.getValue());
                if (it.hasNext()) {
                    signStrBuilder.append("&");
                }
            }
            
            String signStr = signStrBuilder.toString().replace("\"", "");
            log.info("文件上传签名原文: {}", signStr);
            
            // 4. RSA签名
            String sign = rsaSign(signStr, RSA_PRIVATE_KEY);
            log.info("文件上传sign: {}", sign);
            
            // 5. 构建HTTP请求（保持原有结构，只增加必要的header）
            HttpRequest httpRequest = HttpRequest.post(url)
                    .form(filedName, file)
                    .header("appId", APP_ID)
                    .header("nonce", nonce)
                    .header("timestamp", timestamp)
                    .header("sign", sign)
                    .header("version", "v1");

            if (!Strings.isNullOrEmpty(uId)) {
            	 httpRequest.header("uId", uId);
            }
            
            log.info("========== 文件上传请求参数 ==========");
            log.info("请求URL: {}", url);
            log.info("请求方法: POST");
            log.info("请求头参数:");
            if (useProxy) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, 
                    new InetSocketAddress(proxyAddress, proxyPort));
                httpRequest.setProxy(proxy);
            }
            // 6. 发送请求
            httpRequest.timeout(NOTIFY_TIMEOUT)
                    .charset(StandardCharsets.UTF_8)
                    .setConnectionTimeout(NOTIFY_CONNECT_TIMEOUT);
            
            String dataStr = httpRequest.execute().body();
            log.info("文件上传响应: {}", dataStr);
            // 7. 解析响应
            ApiResponseEntity responseEntity = 
                JSONObject.parseObject(dataStr, ApiResponseEntity.class);
            
            if (Constants.ZERO_INT == responseEntity.getCode()) {
                return setResultSuccess(responseEntity.getData(), responseEntity.getMsg());
            } else {
                return setResult(responseEntity.getCode(), 
                    responseEntity.getMsg(), null);
            }
            
        } catch (Exception e) {
            log.error("文件上传异常", e);
            return setResult(Constants.HTTP_RES_CODE_500, 
                "文件上传异常：" + e.getMessage(), null);
        }
    }

    
}