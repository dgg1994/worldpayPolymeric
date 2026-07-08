package com.polymeric.service.admin.impl;

import com.polymeric.base.ResponseBase;
import com.polymeric.dao.admin.StatDao;
import com.polymeric.dao.channel.ChannelInfoDao;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
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
    private StatDao statDao;

    @Resource
    private TokenUtils tokenUtils;

    @Override
    public ResponseBase findList(@RequestBody StatQuery statQuery) {
        boolean adminScope = tokenUtils.isAdmin();
        if (!adminScope) {
            statQuery.setMerchantAppId(tokenUtils.getMerchantAppId());
        }

        MerchantsInfoEntity merchantsInfo = merchantsInfoDao.selectAmount(statQuery);
        Integer channelCount = adminScope ? countChannel() : resolveMerchantChannelCount();
        Integer merchantCount = adminScope ? countMerchant() : 1;

        StatInfoRes infoRes = new StatInfoRes();
        infoRes.setChannelMoney(defaultAmount(statDao.sumChannelMoney(statQuery)));
        infoRes.setChannelCount(channelCount);
        infoRes.setMerchantCount(merchantCount);
        infoRes.setMerchantRechargeTotal(defaultAmount(statDao.sumMerchantRechargeTotal(statQuery)));
        infoRes.setMerchantMoney(defaultAmount(merchantsInfo == null ? null : merchantsInfo.getAvailableAmount()));
        infoRes.setTotalFrozenAmount(defaultAmount(merchantsInfo == null ? null : merchantsInfo.getFreezeAmount()));
        infoRes.setBankCardRechargeTotal(defaultAmount(statDao.sumBankCardRechargeTotal(statQuery)));
        infoRes.setBankCardConsumeTotal(defaultAmount(statDao.sumBankCardConsumeTotal(statQuery)));
        infoRes.setBankCardOpenTotal(defaultAmount(statDao.sumBankCardOpenTotal(statQuery)));
        infoRes.setPlatformIncomeTotal(defaultAmount(statDao.sumPlatformIncomeTotal(statQuery)));
        infoRes.setTransactionHistory(defaultAmount(statDao.sumTransactionHistory(statQuery)));
        infoRes.setChargeAmount(toLongAmount(statDao.sumChargeAmount(statQuery)));
        infoRes.setRecordAmount(toLongAmount(statDao.sumRecordAmount(statQuery)));
        infoRes.setUserRegisterTotal(defaultLong(statDao.countUserRegister(statQuery)));
        infoRes.setBankCardOpenCount(defaultLong(statDao.countBankCardOpen(statQuery)));
        infoRes.setBankCardActiveCount(defaultLong(statDao.countBankCardActive(statQuery)));
        infoRes.setApiRequestCount(defaultLong(statDao.countApiRequest(statQuery)));
        infoRes.setApiDeleteCount(defaultLong(statDao.countApiDelete(statQuery)));
        infoRes.setCallBackCount(defaultLong(statDao.countCallBack(statQuery)));

        return setResultSuccess(infoRes);
    }

    private Integer countChannel() {
        return channelInfoDao.selectCount(null);
    }

    private Integer countMerchant() {
        return merchantsInfoDao.selectCount(null);
    }

    private Integer resolveMerchantChannelCount() {
        MerchantsInfoEntity merchantInfo = tokenUtils.getMerchantInfo();
        if (merchantInfo == null || merchantInfo.getChannelId() == null) {
            return 0;
        }
        return 1;
    }

    private BigDecimal defaultAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }

    private Long defaultLong(Long value) {
        return value == null ? 0L : value;
    }

    private Long toLongAmount(BigDecimal amount) {
        if (amount == null) {
            return 0L;
        }
        return amount.longValue();
    }
}
