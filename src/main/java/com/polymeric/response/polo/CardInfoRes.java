package com.polymeric.response.polo;

import lombok.Data;

/**
 * @category polo银行卡详情信息
 * @author Hlin
 *
 */
@Data
public class CardInfoRes {
	
	private Integer infoType;
	
	private String cardPanUrl;
	
	private String cardNumber;
	
	private String cvv;
	
	private String expireDate;
	
	private Integer status;

}
