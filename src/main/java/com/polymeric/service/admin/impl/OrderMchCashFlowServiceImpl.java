package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.order.OrderMchCashFlowDao;
import com.polymeric.entity.order.OrderMchCashFlowEntity;
import com.polymeric.service.admin.OrderMchCashFlowService;
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
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        QueryWrapper<OrderMchCashFlowEntity> wrapper = new QueryWrapper<>();
        if (entity.getMchId() != null){
            wrapper.eq("mch_id",entity.getMchId());
        }
        if (entity.getMchAppid() != null){
            wrapper.eq("mch_appid",entity.getMchAppid());
        }
        if (entity.getMchOrderNum() != null){
            wrapper.eq("mch_order_num",entity.getMchOrderNum());
        }
        if (entity.getOrderNum() != null){
            wrapper.eq("order_num",entity.getOrderNum());
        }
        if (entity.getOrderState() != null){
            wrapper.eq("order_state",entity.getOrderState());
        }
        if (entity.getOrderType() != null){
            wrapper.eq("order_type",entity.getOrderType());
        }
        if (entity.getTradeType() != null){
            wrapper.eq("trade_type",entity.getTradeType());
        }
        if (entity.getStartTime() != null && entity.getEndTime() != null){
            wrapper.ge("setTime",entity.getStartTime());
            wrapper.le("setTime",entity.getEndTime());
        }
        wrapper.orderByDesc("setTime");
        List<OrderMchCashFlowEntity> list = orderMchCashFlowDao.selectList(wrapper);
        if (list == null){
            list = new ArrayList<>();
        }
        PageInfo<OrderMchCashFlowEntity> info = new PageInfo<>(list);
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