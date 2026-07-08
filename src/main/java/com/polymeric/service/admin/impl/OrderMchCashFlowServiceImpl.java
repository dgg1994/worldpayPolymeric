package com.polymeric.service.admin.impl;

import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.order.OrderMchCashFlowDao;
import com.polymeric.entity.order.OrderMchCashFlowEntity;
import com.polymeric.service.admin.OrderMchCashFlowService;
import com.polymeric.utils.GenericityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.polymeric.base.BaseApiService.setResultSuccess;

/**
 * 类描述：商户资金管理实现
 *
 * @author GeminiSun
 * @date 2026/07/04 09:49
 */
@RestController
@Transactional
@CrossOrigin
@Slf4j
public class OrderMchCashFlowServiceImpl implements OrderMchCashFlowService {

    @Resource
    private OrderMchCashFlowDao orderMchCashFlowDao;

    @Override
    public ResponseBase findList(@RequestBody OrderMchCashFlowEntity entity) {
        List<OrderMchCashFlowEntity> list = orderMchCashFlowDao.selectAll(entity);
        if (list == null){
            list = new ArrayList<>();
        }
        List<OrderMchCashFlowEntity> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
        PageInfo<OrderMchCashFlowEntity> info = new PageInfo<>(pageList);
        info.setTotal(list.size());
        return setResultSuccess(info, Constants.SUCCESS);
    }

    @Override
    public ResponseBase findById(Integer id) {
        OrderMchCashFlowEntity orderMchCashFlowEntity = orderMchCashFlowDao.selectById(id);
        if (orderMchCashFlowEntity == null){
            orderMchCashFlowEntity = new OrderMchCashFlowEntity();
        }
        return setResultSuccess(orderMchCashFlowEntity,Constants.SUCCESS);
    }
}