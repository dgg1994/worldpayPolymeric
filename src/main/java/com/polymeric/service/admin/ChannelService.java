package com.polymeric.service.admin;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.InvocationTargetException;

/**
 * 类描述：上游管理
 *
 * @author GeminiSun
 * @date 2026/07/01 15:32
 */
@RequestMapping("/channel")
@Api(value = "上游管理",tags = "上游管理")
public interface ChannelService {

    @PostMapping("/add")
    @ApiOperation(value = "新增上游", notes = "新增上游", response = ResponseBase.class)
    ResponseBase add(ChannelInfoEntity entity) throws InvocationTargetException, IllegalAccessException;

    @PostMapping("/update")
    @ApiOperation(value = "编辑上游", notes = "编辑上游", response = ResponseBase.class)
    ResponseBase update(ChannelInfoEntity entity);

    @PostMapping("/findList")
    @ApiOperation(value = "上游列表", notes = "上游列表", response = ResponseBase.class)
    ResponseBase findList(ChannelInfoEntity entity);

    @GetMapping("/updateState")
    @ApiOperation(value = "编辑上游状态", notes = "编辑上游状态", response = ResponseBase.class)
    ResponseBase updateState(Integer id,Integer channelStatus);


}