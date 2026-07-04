package com.polymeric.enums;

import com.polymeric.entity.system.DicEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：商户资金交易类型
 *
 * @author GeminiSun
 * @date 2026/07/04 09:27
 */
public enum OrderTradeTypeEnum {

    INCOME(1, "入账"),
    EXPENSE(2, "出账");

    private Integer index;
    private String name;

    private OrderTradeTypeEnum(Integer index, String name) {
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

    public static String getName(int i) {
        OrderTradeTypeEnum[] orderTradeTypeEnums = values();
        for (OrderTradeTypeEnum orderTradeTypeEnum : orderTradeTypeEnums) {
            if (orderTradeTypeEnum.getIndex().equals(i)) {
                return orderTradeTypeEnum.getName();
            }
        }
        return null;
    }

    public static List<DicEntity> getList() {
        OrderTradeTypeEnum[] typeEnums = values();
        List<DicEntity> list = new ArrayList<>();
        for (OrderTradeTypeEnum typeEnum : typeEnums) {
            DicEntity dicEntity = new DicEntity();
            dicEntity.setId(typeEnum.getIndex());
            dicEntity.setName(typeEnum.getName());
            list.add(dicEntity);
        }
        return list;
    }

}
