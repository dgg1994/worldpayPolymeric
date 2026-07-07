package com.polymeric.service.api.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.polymeric.query.webhook.WebhookQuery;
import com.polymeric.response.pub.ApiResponseEntity;
import com.polymeric.service.api.TestApiService;

@RestController
@Transactional
@CrossOrigin
public class TestApiServiceImpl implements TestApiService{

	@Override
	public ApiResponseEntity webHookTest(@RequestBody WebhookQuery query) {
		System.out.println("商户收到回调："+JSON.toJSONString(query));
		return ApiResponseEntity.success();
	}

}
