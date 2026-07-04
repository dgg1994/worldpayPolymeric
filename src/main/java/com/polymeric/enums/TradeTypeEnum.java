package com.polymeric.enums;

import com.polymeric.entity.system.DicEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：交易类型
 *
 * @author GeminiSun
 * @date 2026/07/04 09:18
 */
public enum TradeTypeEnum {
    /*
    170-卡片消费、174-卡预授权、171-卡片退款、172-卡片手续费、173-卡片ATM取现、140-申请卡片、150-卡片充值、175-卡片注销
     */
    APPLY_CARD(140, "APPLY_CARD", "申请卡片"),
    CARD_RECHARGE(150, "CARD_RECHARGE", "卡片充值"),
    CARD_CONSUMPTION(170, "CARD_CONSUMPTION", "卡片消费"),
    CARD_REFUND(171, "CARD_REFUND", "卡片退款"),
    CARD_FEE(172, "CARD_FEE", "卡片手续费"),
    CARD_ATM_WITHDRAWAL(173, "CARD_ATM_WITHDRAWAL", "卡片ATM取现"),
    CARD_PRE_AUTH(174, "CARD_PRE_AUTH", "卡预授权"),
    CARD_CANCEL(175, "CARD_CANCEL", "卡片注销");

    private Integer index;
    private String code;
    private String name;

    private TradeTypeEnum(Integer index, String code, String name) {
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

    public static String getName(int i) {
        TradeTypeEnum[] tradeTypeEnums = values();
        for (TradeTypeEnum tradeTypeEnum : tradeTypeEnums) {
            if (tradeTypeEnum.getCode().equals(i)) {
                return tradeTypeEnum.getName();
            }
        }
        return null;
    }

    public static List<DicEntity> getList() {
        TradeTypeEnum[] typeEnums = values();
        List<DicEntity> list = new ArrayList<>();
        for (TradeTypeEnum typeEnum : typeEnums) {
            DicEntity dicEntity = new DicEntity();
            dicEntity.setId(typeEnum.getIndex());
            dicEntity.setName(typeEnum.getName());
            list.add(dicEntity);
        }
        return list;
    }
}