package com.polymeric.service.admin.impl;

import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.merchants.MerchantsUserCardDao;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;
import com.polymeric.service.admin.MerchantsUserCardService;
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
 * 类描述：商户用户卡实现
 *
 * @author GeminiSun
 * @date 2026/07/02 11:32
 */
@RestController
@Transactional
@CrossOrigin
@Slf4j
public class MerchantsUserCardServiceImpl implements MerchantsUserCardService {

    @Resource
    private MerchantsUserCardDao merchantsUserCardDao;

    @Resource
    private TokenUtils tokenUtils;
    @Override
    public ResponseBase findList(@RequestBody MerchantsUserCardEntity entity) {
        if (!tokenUtils.isAdmin()) {
            entity.setMchId(tokenUtils.getMerchantId());
        }
        List<MerchantsUserCardEntity> list = merchantsUserCardDao.selectAll(entity);
        List<MerchantsUserCardEntity> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
        PageInfo<MerchantsUserCardEntity> info = new PageInfo<>(pageList);
        info.setTotal(list.size());
        return setResultSuccess(info, Constants.SUCCESS);
    }
}