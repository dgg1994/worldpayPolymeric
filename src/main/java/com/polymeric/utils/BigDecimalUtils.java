package com.polymeric.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    /**
     * 扣减金额（可用余额减少）
     */
    public static BigDecimal subtract(BigDecimal source, BigDecimal amount) {
        BigDecimal safeSource = source != null ? source : BigDecimal.ZERO;
        BigDecimal safeAmount = amount != null ? amount : BigDecimal.ZERO;
        return safeSource.subtract(safeAmount);
    }
    
    /**
     * 增加金额（冻结金额增加）
     */
    public static BigDecimal add(BigDecimal source, BigDecimal amount) {
        BigDecimal safeSource = source != null ? source : BigDecimal.ZERO;
        BigDecimal safeAmount = amount != null ? amount : BigDecimal.ZERO;
        return safeSource.add(safeAmount);
    }
    
    /**
     * 判断 source 是否小于 target
     */
    public static boolean isLessThan(BigDecimal source, BigDecimal target) {
        BigDecimal safeSource = source != null ? source : BigDecimal.ZERO;
        BigDecimal safeTarget = target != null ? target : BigDecimal.ZERO;
        return safeSource.compareTo(safeTarget) < 0;
    }
    
    /**
     * 判断 source 是否大于等于 target
     */
    public static boolean isGreaterThanOrEqual(BigDecimal source, BigDecimal target) {
        BigDecimal safeSource = source != null ? source : BigDecimal.ZERO;
        BigDecimal safeTarget = target != null ? target : BigDecimal.ZERO;
        return safeSource.compareTo(safeTarget) >= 0;
    }
}
