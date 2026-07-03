package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsCardDao;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import com.polymeric.service.admin.MerchantsCardService;
import com.polymeric.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.polymeric.base.BaseApiService.setResultError;
import static com.polymeric.base.BaseApiService.setResultSuccess;

/**
 * 类描述：商户商品管理
 *
 * @author GeminiSun
 * @date 2026/07/02 10:27
 */
@RestController
@Transactional
@CrossOrigin
@Slf4j
public class MerchantsCardServiceImpl implements MerchantsCardService {

    @Resource
    private MerchantsCardDao merchantsCardDao;

    @Resource
    private TokenUtils tokenUtils;

    @Override
    public ResponseBase update(@RequestBody MerchantsCardEntity entity) {
        Integer id = entity.getId();
        MerchantsCardEntity merchantsCardEntity = merchantsCardDao.selectById(id);
        if (merchantsCardEntity == null){
            return setResultError("商户商品信息不存在");
        }
        merchantsCardDao.updateById(entity);
        return setResultSuccess();
    }

    @Override
    public ResponseBase findList(@RequestBody MerchantsCardEntity entity) {
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        if (!tokenUtils.isAdmin()){
            entity.setMchId(tokenUtils.getMerchantId());
        }
        List<MerchantsCardEntity> list = merchantsCardDao.selectCardList(entity);
        PageInfo<MerchantsCardEntity> info = new PageInfo<>(list);
        return setResultSuccess(info, Constants.SUCCESS);
    }

    @Override
    public ResponseBase findById(Integer id) {
        MerchantsCardEntity merchantsCardEntity = merchantsCardDao.selectInfoById(id);
        if (merchantsCardEntity == null){
            merchantsCardEntity = new MerchantsCardEntity();
        }
        return setResultSuccess(merchantsCardEntity);
    }
}