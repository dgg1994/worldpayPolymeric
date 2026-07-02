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
    
    
    /**
     * 计算含费率的总金额：总金额 = 原金额 * (1 + 费率)
     * @param amount 原金额
     * @param rate 费率
     * @return 含费率的总金额
     */
    public static BigDecimal calculateWithRate(BigDecimal amount, BigDecimal rate) {
        BigDecimal safeAmount = amount != null ? amount : BigDecimal.ZERO;
        BigDecimal safeRate = rate != null ? rate : BigDecimal.ZERO;
        return safeAmount.multiply(BigDecimal.ONE.add(safeRate));
    }
    
    /**
     * 计算费用：费用 = 原金额 * 费率
     */
    public static BigDecimal calculateFee(BigDecimal amount, BigDecimal rate) {
        BigDecimal safeAmount = amount != null ? amount : BigDecimal.ZERO;
        BigDecimal safeRate = rate != null ? rate : BigDecimal.ZERO;
        return safeAmount.multiply(safeRate);
    }
    
}
