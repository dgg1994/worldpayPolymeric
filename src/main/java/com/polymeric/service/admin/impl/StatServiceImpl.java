package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.polymeric.base.ResponseBase;
import com.polymeric.dao.channel.ChannelInfoDao;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsWebHookMsgDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsWebHookMsgEntity;
import com.polymeric.query.admin.StatQuery;
import com.polymeric.response.api.StatInfoRes;
import com.polymeric.service.admin.StatService;
import com.polymeric.utils.TokenUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static com.polymeric.base.BaseApiService.setResultSuccess;

/**
 * 类描述：数据展示实现
 *
 * @author GeminiSun
 * @date 2026/07/06 10:26
 */
@RestController
@Transactional
@CrossOrigin
public class StatServiceImpl implements StatService {

    @Resource
    private MerchantsInfoDao merchantsInfoDao;

    @Resource
    private ChannelInfoDao channelInfoDao;

    @Resource
    private MerchantsWebHookMsgDao merchantsWebHookMsgDao;

    @Resource
    private TokenUtils tokenUtils;

    @Override
    public ResponseBase findList(@RequestBody StatQuery statQuery) {
        StatInfoRes infoRes = new StatInfoRes();

        //获取当前登录用户
        if (!tokenUtils.isAdmin()) {
            String merchantAppId = tokenUtils.getMerchantAppId();
            statQuery.setMerchantAppId(merchantAppId);
        }
        // 并行执行4个查询
        CompletableFuture<MerchantsInfoEntity> amountFuture =
                CompletableFuture.supplyAsync(() -> merchantsInfoDao.selectAmount(statQuery));
        CompletableFuture<Integer> channelFuture =
                CompletableFuture.supplyAsync(this::countChannel);
        CompletableFuture<Integer> merchantFuture =
                CompletableFuture.supplyAsync(this::countMerchant);
        CompletableFuture<Long> callbackFuture =
                CompletableFuture.supplyAsync(() -> countCallBack(statQuery));

        // 等待所有查询完成
        CompletableFuture.allOf(amountFuture, channelFuture, merchantFuture, callbackFuture).join();

        // 组装结果
        MerchantsInfoEntity merchantsInfo = amountFuture.join();
        infoRes.setMerchantMoney(merchantsInfo == null ? BigDecimal.ZERO : merchantsInfo.getAvailableAmount());
        infoRes.setTotalFrozenAmount(merchantsInfo == null ? BigDecimal.ZERO : merchantsInfo.getFreezeAmount());
        infoRes.setChannelCount(tokenUtils.isAdmin() ? channelFuture.join() : 1);
        infoRes.setMerchantCount(tokenUtils.isAdmin() ? merchantFuture.join() : 1);
        infoRes.setCallBackCount(callbackFuture.join());

        return setResultSuccess(infoRes);
    }

    /**
     * 统计上游渠道数量
     */
    private Integer countChannel() {
        return channelInfoDao.selectCount(null);
    }

    /**
     * 统计商户数量
     */
    private Integer countMerchant() {
        return merchantsInfoDao.selectCount(null);
    }

    /**
     * 统计回调消息数量
     */
    private Long countCallBack(StatQuery statQuery) {
        QueryWrapper<MerchantsWebHookMsgEntity> wrapper = new QueryWrapper<>();
        if (statQuery.getMerchantAppId() != null){
            wrapper.eq("mch_appid", statQuery.getMerchantAppId());
        }
        applyTimeCondition(wrapper, statQuery);
        return merchantsWebHookMsgDao.selectCount(wrapper).longValue();
    }

    /**
     * 公共时间条件填充
     */
    private void applyTimeCondition(QueryWrapper<?> wrapper, StatQuery statQuery) {
        if (statQuery.getStartTime() != null && statQuery.getEndTime() != null) {
            wrapper.between("setTime", statQuery.getStartTime(), statQuery.getEndTime());
        }
    }
}