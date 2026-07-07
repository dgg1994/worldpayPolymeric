package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.channel.ChannelCardDao;
import com.polymeric.dao.merchants.MerchantsCardDao;
import com.polymeric.entity.channel.ChannelCardEntity;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import com.polymeric.service.admin.MerchantsCardService;
import com.polymeric.utils.GenericityUtil;
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

    @Resource
    private ChannelCardDao channelCardDao;

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
        if (!tokenUtils.isAdmin()){
            entity.setMchId(tokenUtils.getMerchantId());
        }
        List<MerchantsCardEntity> list = merchantsCardDao.selectCardList(entity);
        List<MerchantsCardEntity> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
        PageInfo<MerchantsCardEntity> info = new PageInfo<>(pageList);
        info.setTotal(list.size());
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

    @Override
    public ResponseBase updateState(Integer id, Integer merchantsStatus) {
        MerchantsCardEntity merchantsCardEntity = merchantsCardDao.selectById(id);
        if (merchantsCardEntity == null){
            return setResultError("商户商品不存在，请确认信息");
        }
        if (merchantsStatus == 1 && merchantsCardEntity.getCardState() == 2){
            //说明要上架查询上游卡状态
            ChannelCardEntity channelCardEntity = channelCardDao.selectOne(new QueryWrapper<ChannelCardEntity>().eq("card_id", merchantsCardEntity.getCardId()));
            if (channelCardEntity != null && channelCardEntity.getCardState() == 2){
                return setResultError("上游卡已下架，请确认信息");
            }
        }
        merchantsCardEntity.setCardState(merchantsStatus);
        merchantsCardDao.updateById(merchantsCardEntity);
        return setResultSuccess();

    }
}