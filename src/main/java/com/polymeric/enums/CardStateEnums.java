package com.polymeric.enums;

/**
 * @category 卡状态
 * @author Hlin
 *
 */
public enum CardStateEnums {
    
    INITIAL(0, "INITIAL", "初始状态"),
    WAITING_ACTIVATE(1, "WAITING_ACTIVATE", "待激活"),
    ACTIVATING(2, "ACTIVATING", "激活中"),
    NORMAL(3, "NORMAL", "正常"),
    FROZEN(4, "FROZEN", "冻结"),
    LOST(5, "LOST", "挂失"),
    PRE_CANCEL(6, "PRE_CANCEL", "注销前"),
    CANCELED(7, "CANCELED", "注销"),
    EXPIRED(8, "EXPIRED", "过期"),
    SUSPENDED(9, "SUSPENDED", "暂停");
    
    private Integer index;
    private String code;
    private String name;
    
    private CardStateEnums(Integer index, String code, String name) {
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
}