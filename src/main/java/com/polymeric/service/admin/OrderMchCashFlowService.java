package com.polymeric.service.admin;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.order.OrderMchCashFlowEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类描述：商户资金配置
 *
 * @author GeminiSun
 * @date 2026/07/04 09:42
 */
@RequestMapping("/orderMchCashFlow")
@Api(value = "商户资金管理",tags = "商户资金管理")
public interface OrderMchCashFlowService {

    @PostMapping("/findList")
    @ApiOperation(value = "商户资金列表", notes = "商户资金列表", response = ResponseBase.class)
    ResponseBase findList(OrderMchCashFlowEntity entity);

    @GetMapping("/findById")
    @ApiOperation(value = "根据id查询商户资金详情", notes = "根据id查询商户资金详情", response = ResponseBase.class)
    ResponseBase findById(Integer id);
}
