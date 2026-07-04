package com.polymeric.service.admin;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.order.OrderBankCardTradeEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类描述：银行卡交易
 *
 * @author GeminiSun
 * @date 2026/07/04 09:43
 */
@RequestMapping("/orderBankCardTrade")
@Api(value = "银行卡交易管理",tags = "银行卡交易管理")
public interface OrderBankCardTradeService {

    @PostMapping("/findList")
    @ApiOperation(value = "银行卡交易列表", notes = "银行卡交易列表", response = ResponseBase.class)
    ResponseBase findList(OrderBankCardTradeEntity entity);

    @GetMapping("/findById")
    @ApiOperation(value = "根据id查询银行卡交易详情", notes = "根据id查询商户资金详情", response = ResponseBase.class)
    ResponseBase findById(Integer id);
}
