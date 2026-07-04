package com.polymeric.service.admin;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.order.OrderIncomeListEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类描述：平台收益配置
 *
 * @author GeminiSun
 * @date 2026/07/04 09:43
 */
@RequestMapping("/orderIncomeList")
@Api(value = "平台收益管理",tags = "平台收益管理")
public interface OrderIncomeListService {

    @PostMapping("/findList")
    @ApiOperation(value = "平台收益列表", notes = "平台收益列表", response = ResponseBase.class)
    ResponseBase findList(OrderIncomeListEntity entity);

    @GetMapping("/findById")
    @ApiOperation(value = "根据id查询平台收益详情", notes = "根据id查询商户资金详情", response = ResponseBase.class)
    ResponseBase findById(Integer id);
}
