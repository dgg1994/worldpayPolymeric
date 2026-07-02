package com.polymeric.enums;

/**
 * 类描述：交易状态
 *
 * @author GeminiSun
 * @date 2026/07/02 14:45
 */
public enum TxStatusEnums {

    SUCCESS(0, "已确认"),
    WAIT(1, "待确认"),
    ERROR(2, "失败");

    private Integer index;
    private String name;

    private TxStatusEnums(Integer index, String name) {
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