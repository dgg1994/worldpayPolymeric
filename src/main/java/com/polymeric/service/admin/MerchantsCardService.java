package com.polymeric.service.admin;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.merchants.MerchantsCardEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类描述：商户商品管理
 *
 * @author GeminiSun
 * @date 2026/07/02 10:26
 */
@RequestMapping("/merchantsCard")
@Api(value = "商户商品管理",tags = "商户商品管理")
public interface MerchantsCardService {

    @PostMapping("/update")
    @ApiOperation(value = "编辑商户商品", notes = "编辑商户商品", response = ResponseBase.class)
    ResponseBase update(MerchantsCardEntity entity);

    @PostMapping("/findList")
    @ApiOperation(value = "商户商品列表", notes = "商户商品列表", response = ResponseBase.class)
    ResponseBase findList(MerchantsCardEntity entity);

    @GetMapping("/findById")
    @ApiOperation(value = "根据商品id查询商品信息", notes = "根据商品id查询商品信息", response = ResponseBase.class)
    ResponseBase findById(Integer id);

    @GetMapping("/updateState")
    @ApiOperation(value = "编辑商户商品状态", notes = "编辑商户商品状态", response = ResponseBase.class)
    ResponseBase updateState(Integer id,Integer merchantsStatus);

}
