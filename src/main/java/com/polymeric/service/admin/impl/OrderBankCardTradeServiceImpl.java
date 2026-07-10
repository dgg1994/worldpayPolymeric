package com.polymeric.service.admin.impl;

import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.order.OrderBankCardTradeDao;
import com.polymeric.entity.order.OrderBankCardTradeEntity;
import com.polymeric.service.admin.OrderBankCardTradeService;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource
    private TokenUtils tokenUtils;

    @Override
    public ResponseBase findList(@RequestBody OrderBankCardTradeEntity entity) {
        if (!tokenUtils.isAdmin()){
            entity.setMchAppid(tokenUtils.getMerchantAppId());
        }
        List<OrderBankCardTradeEntity> list = orderBankCardTradeDao.selectAll(entity);
        List<OrderBankCardTradeEntity> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
        PageInfo<OrderBankCardTradeEntity> info = new PageInfo<>(pageList);
        info.setTotal(list.size());
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