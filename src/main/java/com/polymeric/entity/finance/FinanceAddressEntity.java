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
 * 类描述：平台首款地址配置
 *
 * @author GeminiSun
 * @date 2026/07/02 14:23
 */
@Data
@TableName("finance_address")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "平台收款地址池", description = "平台收款地址池")
public class FinanceAddressEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(name = "id",value = "主键",required = true,dataType = "Integer")
    private String id;

    @TableField("address")
    @ApiModelProperty(name = "address", value = "收款地址", required = true, dataType = "String")
    private String address;

    @TableField("address_type")
    @ApiModelProperty(name = "addressType", value = "地址类型：TRON/BSC/ETH", required = true, dataType = "String")
    private String addressType;

    @TableField("currency")
    @ApiModelProperty(name = "currency", value = "币种", required = true, dataType = "String")
    private String currency;

    @TableField("min_amount")
    @ApiModelProperty(name = "minAmount", value = "最小收款金额", required = true, dataType = "BigDecimal")
    private BigDecimal minAmount;

    @TableField("status")
    @ApiModelProperty(name = "status", value = "状态：0-启用，1-关闭", required = true, dataType = "String")
    private Integer status;

    @TableField("remark")
    @ApiModelProperty(name = "remark", value = "备注", dataType = "String")
    private String remark;

    @TableField("setTime")
    @ApiModelProperty(name = "setTime",value = "注册时间",required = false,dataType = "Date")
    private Date setTime;

    @TableField("gmtModified")
    @ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
    private Date gmtModified;

    @TableField(exist = false)
    @ApiModelProperty(name = "statusName", value = "状态名称（展示用）", dataType = "String")
    private String statusName;

    @TableField(exist = false)
    @ApiModelProperty(name = "addressTypeName", value = "地址类型名称（展示用）", dataType = "String")
    private String addressTypeName;
}
