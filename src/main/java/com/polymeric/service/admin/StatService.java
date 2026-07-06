package com.polymeric.service.admin;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.channel.ChannelCardEntity;
import com.polymeric.query.admin.StatQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类描述：首页数据展示
 *
 * @author GeminiSun
 * @date 2026/07/06 10:23
 */
@RequestMapping("/stat")
@Api(value="平台首页数据展示",tags = "平台首页数据展示")
public interface StatService {

    @PostMapping("/total")
    @ApiOperation(value = "首页信息展示", notes = "首页信息展示", response = ResponseBase.class)
    ResponseBase findList(StatQuery statQuery);
}
