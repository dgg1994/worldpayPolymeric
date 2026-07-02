package com.polymeric.enums;

/**
 * 类描述：审核类型
 *
 * @author GeminiSun
 * @date 2026/07/02 14:43
 */
public enum RecordTypeEnums {

    ON_CHAIN(1, "自动链上"),
    MANUAL(2, "人工处理");

    private Integer index;
    private String name;

    private RecordTypeEnums(Integer index, String name) {
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
