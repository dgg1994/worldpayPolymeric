package com.polymeric.service.admin;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.finance.FinanceAddressEntity;
import com.polymeric.entity.finance.FinanceRechargeRecordEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.InvocationTargetException;

/**
 * 类描述：财务管理
 *
 * @author GeminiSun
 * @date 2026/07/02 14:47
 */
@RequestMapping("/finance")
@Api(value = "财务管理",tags = "财务管理")
public interface FinanceService {

    @GetMapping("/financeList")
    @ApiOperation(value = "财务设置", notes = "财务设置", response = ResponseBase.class)
    ResponseBase financeList();

    @PostMapping("/financeAdd")
    @ApiOperation(value = "新增财务地址", notes = "新增财务地址", response = ResponseBase.class)
    ResponseBase financeAdd(FinanceAddressEntity entity) throws InvocationTargetException, IllegalAccessException;

    @PostMapping("/financeUpdate")
    @ApiOperation(value = "更新财务地址", notes = "更新财务地址", response = ResponseBase.class)
    ResponseBase financeUpdate(FinanceAddressEntity entity);

    @GetMapping("/financeStatus")
    @ApiOperation(value = "修改财务地址状态", notes = "修改财务地址状态", response = ResponseBase.class)
    ResponseBase financeStatus(Integer id, Integer status);

    @PostMapping("/recordList")
    @ApiOperation(value = "充值记录", notes = "充值记录", response = ResponseBase.class)
    ResponseBase recordList(FinanceRechargeRecordEntity entity);

    @GetMapping("/financeCheck")
    @ApiOperation(value = "人工审核处理", notes = "人工审核处理", response = ResponseBase.class)
    ResponseBase financeCheck(Integer id,Integer status);


}
