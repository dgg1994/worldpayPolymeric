package com.polymeric.enums;


import com.polymeric.entity.system.DicEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单类型
 * 
 * @author system
 * @date 2026-06-16
 */
public enum OrderTypeEnum {

    OPEN_CARD(1, "开卡",2),
    CARD_TOP_UP(2, "银行卡充值",2),
    BALANCE_TOP_UP(3, "余额充值",1),
    CANCEL_CARD(4, "注销卡片",1);

    private final Integer code;
    
    private final String name;
    
    private final Integer lable;

    OrderTypeEnum(Integer code, String name,Integer lable) {
        this.code = code;
        this.name = name;
        this.lable = lable;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

	public Integer getLable() {
		return lable;
	}

    public static String getName(int i) {
        OrderTypeEnum[] orderTradeTypeEnums = values();
        for (OrderTypeEnum orderTradeTypeEnum : orderTradeTypeEnums) {
            if (orderTradeTypeEnum.getCode().equals(i)) {
                return orderTradeTypeEnum.getName();
            }
        }
        return null;
    }

    public static List<DicEntity> getList() {
        OrderTypeEnum[] typeEnums = values();
        List<DicEntity> list = new ArrayList<>();
        for (OrderTypeEnum typeEnum : typeEnums) {
            DicEntity dicEntity = new DicEntity();
            dicEntity.setId(typeEnum.getCode());
            dicEntity.setName(typeEnum.getName());
            list.add(dicEntity);
        }
        return list;
    }
}