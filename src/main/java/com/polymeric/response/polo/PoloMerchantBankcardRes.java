package com.polymeric.response.polo;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PoloMerchantBankcardRes {
	
	@ApiModelProperty(name = "mchAppid",value = "商户编号",required = true,dataType = "String")
    private String mchAppid;

	@ApiModelProperty(name = "cardId",value = "三方卡片id",required = true,dataType = "String")
	private Integer cardId;
	
	@ApiModelProperty(name = "cardBin",value = "关联BIN码",required = true,dataType = "String")
	private String cardBin;
	
	@ApiModelProperty(name = "bankCardNature",value = "卡类型：VIRTUAL、PHYSICAL",required = true,dataType = "String")
	private String bankCardNature;
	
	@ApiModelProperty(name = "bankCardNature",value = "卡品牌",required = true,dataType = "String")
	private String cardBrand;
	
	@ApiModelProperty(name = "cardMode",value = "卡模式 NORMAL 常规卡 ;SHARE 共享卡",required = true,dataType = "String")
	private String cardMode;
	
	@ApiModelProperty(name = "ccy",value = "卡消费币种",required = true,dataType = "String")
	private String ccy;
	
	@ApiModelProperty(name = "applyFee",value = "开卡费用，开卡一次收取",required = true,dataType = "String")
	private BigDecimal applyFee;
	
	@ApiModelProperty(name = "rechargeFee",value = "充值手续费比例，0-1之间",required = true,dataType = "String")
	private BigDecimal rechargeFee;
	
	@ApiModelProperty(name = "bankcardRegion",value = "卡片区域",required = true,dataType = "String")
	private String bankcardRegion;
	
	@ApiModelProperty(name = "activeMinLimit",value = "虚拟卡激活首次充值最小金额",required = true,dataType = "String")
	private BigDecimal activeMinLimit;
	
	@ApiModelProperty(name = "rechargeMinLimit",value = "单笔充值最小金额",required = true,dataType = "String")
	private BigDecimal rechargeMinLimit;

}
