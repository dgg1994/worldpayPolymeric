package com.polymeric.service.api.impl;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsKeyDao;
import com.polymeric.entity.merchants.MerchantsKeyEntity;
import com.polymeric.query.api.TestQuery;
import com.polymeric.query.webhook.WebhookQuery;
import com.polymeric.service.api.TestApiService;
import com.polymeric.utils.sign.RsaSignUtil;

import cn.hutool.http.HttpRequest;

@RestController
@Transactional
@CrossOrigin
public class TestApiServiceImpl extends BaseApiService implements TestApiService{
	
	@Autowired
	private MerchantsKeyDao merchantsKeyDao;

	@Override
	public ResponseBase webHookTest(@RequestBody WebhookQuery query) {
		System.out.println("商户收到回调："+JSON.toJSONString(query));
		return setResultSuccess();
	}

	@Override
	public ResponseBase send(@RequestBody TestQuery query) {
		try {
			MerchantsKeyEntity keyEntity = merchantsKeyDao.findAppId(query.getAppid());
			if(keyEntity == null) {
				return setResultError("商户不存在");
			}
			if(query.getUid().isEmpty()) {
				query.setUid(null);
			}
			if("post".equals(query.getQueryType())) {
				ResponseBase base = this.postData(query,keyEntity);
				return base;
			}else {
				ResponseBase base = this.getData(query,keyEntity);
				return base;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	
	
	public ResponseBase postData(TestQuery query, MerchantsKeyEntity keyEntity) {
        try {
        	System.out.println("商户测试请求接口："+query.getUrl());
        	System.out.println("商户测试请求接口："+query.getUrl());
            String nonce = generateNonce();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String sign = RsaSignUtil.signRequest(query.getAppid(),nonce,timestamp,JSON.toJSONString(query), keyEntity.getPrivateKey());
            // 3. 构建HTTP请求
            HttpRequest httpRequest = HttpRequest.post(query.getUrl())
                    .header("appId", query.getAppid())
                    .header("nonce", nonce)
                    .header("timestamp", timestamp)
                    .header("sign", sign);
            if (!Strings.isNullOrEmpty(query.getUid())) {
	           	 httpRequest.header("uId", query.getUid());
	           }
            // 4. 发送请求
            String dataStr = httpRequest
                    .timeout(30000)
                    .body(JSON.toJSONString(query.getData()))
                    .body(JSON.toJSONString(query.getData()))
                    .charset(StandardCharsets.UTF_8)
                    .setConnectionTimeout(5000)
                    .execute()
                    .body();
            // 5. 解析响应
            ResponseBase base = JSONObject.parseObject(dataStr, ResponseBase.class);
            return base;
			
        } catch (Exception e) {
            return setResult(Constants.HTTP_RES_CODE_500,
                    "系统异常：" + e.getMessage(), null);
        }
    }
	
	
	
	public ResponseBase getData(TestQuery query, MerchantsKeyEntity keyEntity) {
        try {
        	System.out.println("商户测试请求接口："+query.getUrl());
            String nonce = generateNonce();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String sign = RsaSignUtil.signRequest(query.getAppid(),nonce,timestamp,JSON.toJSONString(query), keyEntity.getPrivateKey());
            // 3. 构建HTTP请求
            HttpRequest httpRequest = HttpRequest.get(query.getUrl())
                    .header("appId", query.getAppid())
                    .header("nonce", nonce)
                    .header("timestamp", timestamp)
                    .header("sign", sign);
            if (!Strings.isNullOrEmpty(query.getUid())) {
	           	 httpRequest.header("uId", query.getUid());
	           }
            // 4. 发送请求
            String dataStr = httpRequest
                    .timeout(30000)
                    .charset(StandardCharsets.UTF_8)
                    .setConnectionTimeout(5000)
                    .execute()
                    .body();
            // 5. 解析响应
            ResponseBase base = JSONObject.parseObject(dataStr, ResponseBase.class);
            return base;
			
        } catch (Exception e) {
            return setResult(Constants.HTTP_RES_CODE_500,
                    "系统异常：" + e.getMessage(), null);
        }
    }
	
	
	
    private static String generateNonce() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
	

}
