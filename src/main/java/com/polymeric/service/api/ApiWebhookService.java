package com.polymeric.service.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.query.webhook.WebhookQuery;
import com.polymeric.response.pub.ApiResponseEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/webhook")
@Api(value = "三方回调地址",tags = "三方回调地址")
public interface ApiWebhookService {
	
	@PostMapping("/polo/agentNotify")
	@ApiOperation(value = "polo三方回调",notes = "polo三方回调",response = ApiResponseEntity.class)
	ApiResponseEntity agentNotify(WebhookQuery entity);

}
