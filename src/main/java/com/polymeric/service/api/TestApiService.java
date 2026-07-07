package com.polymeric.service.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.query.api.TestQuery;
import com.polymeric.query.webhook.WebhookQuery;
import com.polymeric.response.pub.ApiResponseEntity;

@RequestMapping("/api/test")
public interface TestApiService {
	
	@PostMapping("/webhook")
	ApiResponseEntity webHookTest(WebhookQuery query);
	
	@PostMapping("/send")
	ResponseBase send(TestQuery query);

}
