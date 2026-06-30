package com.polymeric.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountConvertUtil {
	/**
     * 将"分"转换为"元"（BigDecimal）
     * 
     * @param amountInFen 金额（单位：分），如 100 表示 1.00 元
     * @return 金额（单位：元），如 1.00
     */
    public static BigDecimal fenToYuan(Integer amountInFen) {
        if (amountInFen == null) {
            return BigDecimal.ZERO;
        }
        // 分转元：除以100，保留2位小数，四舍五入
        return new BigDecimal(amountInFen)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }
    
    public static BigDecimal fenToYuan(Long amountInFen) {
        if (amountInFen == null) {
            return BigDecimal.ZERO;
        }
        // 分转元：除以100，保留2位小数，四舍五入
        return new BigDecimal(amountInFen)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }
    
    /**
     * 将"元"转换为"分"（Integer）
     * 
     * @param amountInYuan 金额（单位：元），如 1.00
     * @return 金额（单位：分），如 100
     */
    public static Long yuanToFen(BigDecimal amountInYuan) {
        if (amountInYuan == null) {
            return 0L;
        }
        // 元转分：乘以100，去掉小数位
        return amountInYuan.multiply(new BigDecimal(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();
    }
    
    
    
    /**
     * 金额元转分
     * @param amount 金额元（String类型）
     * @return 金额分（long类型）
     */
    public static long yuanToFenStr(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return 0L;
        }
        try {
            // 使用 BigDecimal 避免精度丢失
            BigDecimal yuan = new BigDecimal(amount.trim());
            // 元转分：乘以100
            BigDecimal fen = yuan.multiply(new BigDecimal(100));
            // 返回long类型（保留0位小数，四舍五入）
            return fen.setScale(0, RoundingMode.HALF_UP).longValue();
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    
    
}