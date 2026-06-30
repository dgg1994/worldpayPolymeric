package com.polymeric.response.pub;


import com.polymeric.constants.Constants;
import com.polymeric.enums.PublicEnums;

import lombok.Data;

@Data
public class ApiResponseEntity {
	
	private Integer code;
	
	private String msg;

	private Object data;
	
	public static ApiResponseEntity error() {
		ApiResponseEntity response = new ApiResponseEntity();
		response.setCode(PublicEnums.ONE_HUNDRED.getIndex());
		response.setMsg(Constants.API_RES_E);
		return response;
	}
	
	public static ApiResponseEntity success() {
		ApiResponseEntity response = new ApiResponseEntity();
		response.setCode(PublicEnums.ZERO.getIndex());
		response.setMsg(Constants.API_RES_W);
		return response;
	}
	
	public static ApiResponseEntity successMsg(String msg) {
		ApiResponseEntity response = new ApiResponseEntity();
		response.setCode(PublicEnums.ZERO.getIndex());
		response.setMsg(msg);
		return response;
	}
	
	public static ApiResponseEntity setResult(Integer code, String msg, Object data) {
		ApiResponseEntity response = new ApiResponseEntity();
		response.setCode(code);
		response.setMsg(msg);
		return response;
	}
	
	

}
