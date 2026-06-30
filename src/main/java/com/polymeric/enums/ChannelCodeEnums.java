package com.polymeric.enums;


/**
 * @category 币种
 * @author Hlin
 *
 */
public enum ChannelCodeEnums {
	POLO(1, "10001", "POLO");
	

	private Integer index;

	private String code;

	private String name;

	private ChannelCodeEnums(Integer index, String code, String value) {
		this.index = index;
		this.code = code;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
