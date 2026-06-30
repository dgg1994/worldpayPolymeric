package com.polymeric.enums;

import java.util.ArrayList;
import java.util.List;

import com.polymeric.entity.system.DicEntity;

/**
 * 订单状态枚举
 * 
 * @author system
 * @date 2026-06-16
 */
public enum OrderStatusEnum {

    WAIT_PAY(0, "待支付"),
    
    SUCCESS(1, "支付成功"),
    
    FAIL(2, "支付失败"),
    
    REFUND(3, "已退款"),
    
    CLOSED(4, "已关闭"),
    
    NOT_OUT_CODE(5, "未出码"),
    
    EXCEPTION(6, "异常");

    private final Integer code;
    
    private final String name;

    OrderStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    
    public static String getName(int i) {
    	OrderStatusEnum[] orderStatusEnums = values();
        for (OrderStatusEnum orderStatusEnum : orderStatusEnums) {
            if (orderStatusEnum.getCode().equals(i)) {
                return orderStatusEnum.getName();
            }
        }
        return null;
    }
    
    public static List<DicEntity> getList() {
    	OrderStatusEnum[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (OrderStatusEnum typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getCode());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}
}