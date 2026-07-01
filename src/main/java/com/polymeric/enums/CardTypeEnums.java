package com.polymeric.enums;

/**
 * @category 卡类型
 * @author Hlin
 *
 */
public enum CardTypeEnums {
    
	VIRTUAL(1, "VIRTUAL", "虚拟卡"),
	PHYSICAL(2, "PHYSICAL", "实体卡");
    
    private Integer index;
    private String code;
    private String name;
    
    private CardTypeEnums(Integer index, String code, String name) {
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