package com.polymeric.utils;

import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.response.pub.ApiResponseEntity;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallbackHttpSendUtil extends ApiResponseEntity{

	public static final int NOTIFY_TIMEOUT = 15000;

	public static final int NOTIFY_CONNECT_TIMEOUT = 1000;

	public static ApiResponseEntity forwardData(String url, String pram) {
	    try {
	        System.out.println("回调商户地址：" + url);
	        System.out.println("回调商户内容：" + pram);
	        
	        HttpRequest httpRequest = HttpRequest.post(url);
	        String dataStr = httpRequest
	                .timeout(NOTIFY_TIMEOUT)
	                .body(pram)
	                .contentType("application/json")
	                .charset(StandardCharsets.UTF_8)
	                .setConnectionTimeout(NOTIFY_CONNECT_TIMEOUT)
	                .execute()
	                .body();
	        
	        log.info("响应原始数据: {}", dataStr);
	        
	        if (dataStr != null && !dataStr.isEmpty()) {
	            String trimData = dataStr.trim();
	            // ✅ 判断响应格式
	            if (trimData.startsWith("{")) {
	                // JSON 对象格式
	                return JSONObject.parseObject(trimData, ApiResponseEntity.class);
	            } else if (trimData.startsWith("[")) {
	                return error();
	            } else {
	                // 纯文本格式（如 "success"、"ok" 等）
	                ApiResponseEntity response = new ApiResponseEntity();
	                // 判断是否为成功标识
	                if ("success".equalsIgnoreCase(trimData) 
	                        || "ok".equalsIgnoreCase(trimData)
	                        || "true".equalsIgnoreCase(trimData)) {
	                    response.setCode(200);
	                    response.setMsg(trimData);
	                } else {
	                    response.setCode(500);
	                    response.setMsg(trimData);
	                }
	                return response;
	            }
	        } else {
	            return error();
	        }
	    } catch (Exception e) {
	        log.error("请求异常", e);
	        e.printStackTrace();
	        return error();
	    }
	}

	public static ApiResponseEntity error() {
	    ApiResponseEntity response = new ApiResponseEntity();
	    response.setCode(500);
	    response.setMsg("回调失败");
	    return response;
	}

}
