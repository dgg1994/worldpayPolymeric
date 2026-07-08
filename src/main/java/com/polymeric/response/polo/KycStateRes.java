package com.polymeric.response.polo;

import lombok.Data;

@Data
public class KycStateRes {

	private String status;
	
	private String failedReason;
}
