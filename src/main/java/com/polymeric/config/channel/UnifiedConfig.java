package com.polymeric.config.channel;

import lombok.Data;

@Data
public class UnifiedConfig {
	
	private String apiUrl;
	
	private String appId;
	
	private String rsaPrivateKey;
	
	private String aesKey;

}
