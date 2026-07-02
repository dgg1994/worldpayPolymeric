package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsUserDao;
import com.polymeric.entity.merchants.MerchantsUserEntity;
import com.polymeric.service.admin.MerchantsUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.polymeric.base.BaseApiService.setResultSuccess;

/**
 * 类描述：商户用户实现
 *
 * @author GeminiSun
 * @date 2026/07/02 11:25
 */
@RestController
@Transactional
@CrossOrigin
@Slf4j
public class MerchantsUserServiceImpl implements MerchantsUserService {

    @Resource
    private MerchantsUserDao merchantsUserDao;

    @Override
    public ResponseBase findList(@RequestBody  MerchantsUserEntity entity) {
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        QueryWrapper<MerchantsUserEntity> wrapper = new QueryWrapper<>();
        String userEmail = entity.getUserEmail();
        if (StringUtils.isNoneBlank(userEmail)){
            wrapper.eq("user_email",userEmail);
        }
        Integer channelId = entity.getChannelId();
        if (channelId != null){
            wrapper.eq("channel_id",channelId);
        }
        Integer mchId = entity.getMchId();
        if (mchId != null){
            wrapper.eq("mch_id",mchId);
        }
        List<MerchantsUserEntity> list = merchantsUserDao.selectList(wrapper);
        PageInfo<MerchantsUserEntity> info = new PageInfo<>(list);
        return setResultSuccess(info, Constants.SUCCESS);
    }
}