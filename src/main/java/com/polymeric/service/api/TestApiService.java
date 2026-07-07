package com.polymeric.service.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.query.webhook.WebhookQuery;
import com.polymeric.response.pub.ApiResponseEntity;

@RequestMapping("/test")
public interface TestApiService {
	
	@PostMapping("/webhook")
	ApiResponseEntity webHookTest(WebhookQuery query);

}
