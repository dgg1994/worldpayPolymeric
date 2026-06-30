package com.polymeric.query.sign;

import com.polymeric.entity.merchants.MerchantsKeyEntity;

import lombok.Data;

@Data
public class SignParams {

	private String appId;
	
	private String nonce;
	
	private String timestamp;
	
	private String sign;
	
	private String body;
	
	private MerchantsKeyEntity keyEntity;

}
