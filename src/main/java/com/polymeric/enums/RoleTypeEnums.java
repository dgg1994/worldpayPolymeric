package com.polymeric.enums;


public enum RoleTypeEnums {

    ADMIN(1, "超级管理员","admin"),
    MERCHANTS(2, "商户","merchants"),
    OPERATIONS(3, "运营","operations");
	
    private Integer index;

    private String name;

    private String value;

    private RoleTypeEnums(Integer index, String name, String value) {
        this.index = index;
        this.name = name;
        this.value = value;
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


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
    
    
}
