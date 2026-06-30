package com.polymeric.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse {

	private Integer code;
	
	private String msg;
	
	private String url;
	
	private String path;
	
	private String filename;
	
	private Integer lable;

	public FileResponse() {
		super();
	}

	public FileResponse(Integer code, String msg, String url, String path, String filename, Integer lable) {
		super();
		this.code = code;
		this.msg = msg;
		this.url = url;
		this.path = path;
		this.filename = filename;
		this.lable = lable;
	}
	
	

}
