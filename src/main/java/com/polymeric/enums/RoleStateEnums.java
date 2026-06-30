package com.polymeric.enums;

public enum RoleStateEnums {

    ROLE_DEL_FLAG_NORMAL("0", "存在"),
    ROLE_DEL_FLAG_DELETE("2", "删除"),
    ROLE_STATUS_NORMAL("0", "正常"),
    ROLE_STATUS_STOP("1", "停用");
	
    private String index;

    private String name;


    private RoleStateEnums(String index, String name) {
        this.index = index;
        this.name = name;
    }


    public String getIndex() {
        return index;
    }


    public void setIndex(String index) {
        this.index = index;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


}
