package com.polymeric.response.polo;

import lombok.Data;

@Data
public class CardBalanceRes {
	
	private String balance;
	
	private String currency;
	
	private String symbol;
	
	private String cardLimit;

}
