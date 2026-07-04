package com.polymeric.entity.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.polymeric.query.pub.PageQueryHelperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：财务充值记录
 *
 * @author GeminiSun
 * @date 2026/07/02 14:25
 */
@Data
@TableName("finance_recharge_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "财务充值记录", description = "财务充值记录")
public class FinanceRechargeRecordEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "主键ID", required = true, dataType = "String")
    private String id;

    @TableField("merchant_id")
    @ApiModelProperty(name = "merchantId", value = "商户ID", required = true, dataType = "String")
    private String merchantId;

    @TableField("finance_address")
    @ApiModelProperty(name = "financeAddress", value = "收款地址", dataType = "String")
    private String financeAddress;

    @TableField("amount")
    @ApiModelProperty(name = "amount", value = "充值金额", required = true, dataType = "BigDecimal")
    private BigDecimal amount;

    @TableField("tx_status")
    @ApiModelProperty(name = "txStatus", value = "交易状态：已确认/待确认/失败", required = true, dataType = "String")
    private String txStatus;

    @TableField("record_type")
    @ApiModelProperty(name = "recordType", value = "记录类型：自动链上/人工处理", required = true, dataType = "String")
    private String recordType;

    @TableField("remark")
    @ApiModelProperty(name = "remark", value = "备注", required = true, dataType = "String")
    private String remark;

    @TableField("operator")
    @ApiModelProperty(name = "operator", value = "操作人", dataType = "String")
    private String operator;

    @TableField("setTime")
    @ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;

    @TableField("gmtModified")
    @ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
    private Date gmtModified;

    @TableField(exist = false)
    @ApiModelProperty(name = "txStatusName", value = "交易状态名称（展示用）", dataType = "String")
    private String txStatusName;

    @TableField(exist = false)
    @ApiModelProperty(name = "recordTypeName", value = "记录类型名称（展示用）", dataType = "String")
    private String recordTypeName;

    @TableField(exist = false)
    @ApiModelProperty(name = "merchantName", value = "商户名称（关联查询用）", dataType = "String")
    private String merchantName;
}