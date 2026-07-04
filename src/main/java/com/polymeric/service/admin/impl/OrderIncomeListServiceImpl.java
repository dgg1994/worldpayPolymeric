package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.order.OrderIncomeListDao;
import com.polymeric.entity.order.OrderIncomeListEntity;
import com.polymeric.service.admin.OrderIncomeListService;
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
 * 类描述：平台收益实现
 *
 * @author GeminiSun
 * @date 2026/07/04 09:51
 */
@RestController
@Transactional
@CrossOrigin
@Slf4j
public class OrderIncomeListServiceImpl implements OrderIncomeListService {

    @Resource
    private OrderIncomeListDao orderIncomeListDao;

    @Override
    public ResponseBase findList(@RequestBody OrderIncomeListEntity entity) {
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        QueryWrapper<OrderIncomeListEntity> wrapper = new QueryWrapper<>();
        if (entity.getCardId() != null){
            wrapper.eq("card_id",entity.getCardId());
        }
        if (entity.getCardType() != null){
            wrapper.eq("cardType",entity.getCardType());
        }
        if (entity.getChannelId() != null){
            wrapper.eq("channel_id",entity.getChannelId());
        }
        if (entity.getChannelCode() != null){
            wrapper.eq("channel_code",entity.getChannelCode());
        }
        if (entity.getUserUid() != null){
            wrapper.eq("user_uid",entity.getUserUid());
        }
        if (entity.getUserBankcardId() != null){
            wrapper.eq("user_bankcard_id",entity.getUserBankcardId());
        }
        if (entity.getMchAppid() != null){
            wrapper.eq("mch_appid",entity.getMchAppid());
        }
        if (entity.getOrderNum() != null){
            wrapper.eq("order_num",entity.getOrderNum());
        }
        if (entity.getOrderType() != null){
            wrapper.eq("order_type",entity.getOrderType());
        }
        if (entity.getOrderState() != null){
            wrapper.eq("order_state",entity.getOrderState());
        }
        if (entity.getStartTime() != null && entity.getEndTime() != null){
            wrapper.ge("setTime",entity.getStartTime());
            wrapper.le("setTime",entity.getEndTime());
        }
        wrapper.orderByDesc("setTime");
        List<OrderIncomeListEntity> list = orderIncomeListDao.selectList(wrapper);
        if (list == null){
            list = new ArrayList<>();
        }
        PageInfo<OrderIncomeListEntity> info = new PageInfo<>(list);
        return setResultSuccess(info, Constants.SUCCESS);
    }

    @Override
    public ResponseBase findById(Integer id) {
        OrderIncomeListEntity orderIncomeListEntity = orderIncomeListDao.selectById(id);
        if (orderIncomeListEntity == null){
            orderIncomeListEntity = new OrderIncomeListEntity();
        }
        return setResultSuccess(orderIncomeListEntity, Constants.SUCCESS);
    }
}