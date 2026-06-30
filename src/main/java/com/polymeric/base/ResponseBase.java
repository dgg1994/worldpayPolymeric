package com.polymeric.base;

import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author csz
 *
 */
@Getter
@Setter
public class ResponseBase {

	private Integer code;
	private String msg;
	private Object data;
	
	
	public ResponseBase() {
		super();
	}
	public ResponseBase(Integer code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	

}
