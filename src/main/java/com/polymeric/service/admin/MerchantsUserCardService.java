package com.polymeric.service.admin;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.merchants.MerchantsUserCardEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类描述：商户用户卡管理
 *
 * @author GeminiSun
 * @date 2026/07/02 11:24
 */
@RequestMapping("/merchantsUserCard")
@Api(value = "商户用户卡管理",tags = "商户用户卡管理")
public interface MerchantsUserCardService {

    @PostMapping("/findList")
    @ApiOperation(value = "商户用户卡列表", notes = "商户用户卡列表", response = ResponseBase.class)
    ResponseBase findList(MerchantsUserCardEntity entity);
}
