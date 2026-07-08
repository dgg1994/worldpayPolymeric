package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsUserDao;
import com.polymeric.dao.merchants.MerchantsUserKycDao;
import com.polymeric.entity.merchants.MerchantsUserEntity;
import com.polymeric.entity.merchants.MerchantsUserKycEntity;
import com.polymeric.service.admin.MerchantsUserService;
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

    @Resource
    private MerchantsUserKycDao merchantsUserKycDao;

    @Resource
    private TokenUtils tokenUtils;

    @Override
    public ResponseBase findList(@RequestBody  MerchantsUserEntity entity) {
        if (!tokenUtils.isAdmin()) {
            entity.setMchId(tokenUtils.getMerchantId());
        }
        List<MerchantsUserEntity> list = merchantsUserDao.selectAll(entity);
        List<MerchantsUserEntity> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
        PageInfo<MerchantsUserEntity> info = new PageInfo<>(pageList);
        info.setTotal(list.size());
        return setResultSuccess(info, Constants.SUCCESS);
    }

    @Override
    public ResponseBase findById(Integer id) {
        MerchantsUserEntity merchantsUserEntity = merchantsUserDao.selectById(id);
        if (merchantsUserEntity != null) {
            QueryWrapper<MerchantsUserKycEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id",merchantsUserEntity.getId());
            wrapper.eq("user_uid",merchantsUserEntity.getApiUid());
            MerchantsUserKycEntity merchantsUserKycEntity = merchantsUserKycDao.selectOne(wrapper);
            if (merchantsUserKycEntity != null){
                merchantsUserEntity.setMerchantsUserKycEntity(merchantsUserKycEntity);
            }
        }else {
            merchantsUserEntity = new MerchantsUserEntity();
        }
        return setResultSuccess(merchantsUserEntity);
    }
}