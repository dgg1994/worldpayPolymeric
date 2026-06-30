package com.polymeric.enums;



/**
 * @category 签名请求头
 * @author Hlin
 *
 */
public enum SignHeardEnums {
	APPID(1, "appId"),
	NONCE(2, "nonce"),
	TIMESTAMP(3, "timestamp"),
	VERSION(4, "version"),
	SIGN(5, "sign"),
	UID(6, "uid");
	

	private Integer index;

	private String name;

	private SignHeardEnums(Integer index, String name) {
		this.index = index;
		this.name = name;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
