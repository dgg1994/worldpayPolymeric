package com.polymeric.enums;

/**
 * 类描述：通用状态配置
 *
 * @author GeminiSun
 * @date 2026/07/02 15:10
 */
public enum UniversalEnums {

    OPEN(0, "开启"),
    CLOSE(1, "关闭");

    private Integer index;
    private String name;

    private UniversalEnums(Integer index, String name) {
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
