package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.order.OrderBankCardTradeDao;
import com.polymeric.entity.order.OrderBankCardTradeEntity;
import com.polymeric.service.admin.OrderBankCardTradeService;
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
 * 类描述：银行卡交易实现
 *
 * @author GeminiSun
 * @date 2026/07/04 09:50
 */
@RestController
@Transactional
@CrossOrigin
@Slf4j
public class OrderBankCardTradeServiceImpl implements OrderBankCardTradeService {

    @Resource
    private OrderBankCardTradeDao orderBankCardTradeDao;

    @Override
    public ResponseBase findList(@RequestBody OrderBankCardTradeEntity entity) {
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        QueryWrapper<OrderBankCardTradeEntity> wrapper = new QueryWrapper<>();
        if (entity.getUserUid() != null){
            wrapper.eq("user_uid",entity.getUserUid());
        }
        if (entity.getUserBankcardId() != null){
            wrapper.eq("user_bankcard_id",entity.getUserBankcardId());
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
        if (entity.getTradeType() != null){
            wrapper.eq("trade_type",entity.getTradeType());
        }
        if (entity.getStartTime() != null && entity.getEndTime() != null){
            wrapper.ge("setTime",entity.getStartTime());
            wrapper.le("setTime",entity.getEndTime());
        }
        wrapper.orderByDesc("setTime");
        List<OrderBankCardTradeEntity> list = orderBankCardTradeDao.selectList(wrapper);
        if (list == null){
            list = new ArrayList<>();
        }
        PageInfo<OrderBankCardTradeEntity> info = new PageInfo<>(list);
        return setResultSuccess(info, Constants.SUCCESS);
    }

    @Override
    public ResponseBase findById(Integer id) {
        OrderBankCardTradeEntity orderBankCardTradeEntity = orderBankCardTradeDao.selectById(id);
        if (orderBankCardTradeEntity == null){
            orderBankCardTradeEntity = new OrderBankCardTradeEntity();
        }
        return setResultSuccess(orderBankCardTradeEntity, Constants.SUCCESS);
    }
}