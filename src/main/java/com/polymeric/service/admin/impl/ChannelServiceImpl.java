package com.polymeric.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.polymeric.base.ResponseBase;
import com.polymeric.constants.Constants;
import com.polymeric.dao.channel.ChannelInfoDao;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.entity.merchants.MerchantsIpEntity;
import com.polymeric.service.admin.ChannelService;
import com.polymeric.utils.GenericityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import static com.polymeric.base.BaseApiService.setResultError;
import static com.polymeric.base.BaseApiService.setResultSuccess;

/**
 * 类描述：上游管理实现
 *
 * @author GeminiSun
 * @date 2026/07/01 15:55
 */
@RestController
@Transactional
@CrossOrigin
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelInfoDao channelInfoDao;

    @Override
    public ResponseBase add(@RequestBody ChannelInfoEntity entity) throws InvocationTargetException, IllegalAccessException {
        // 不能重复添加
        QueryWrapper<ChannelInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("channel_code",entity.getChannelCode());
        wrapper.eq("app_Id",entity.getAppId());
        ChannelInfoEntity channelInfoEntity = channelInfoDao.selectOne(wrapper);
        if (channelInfoEntity != null){
            return setResultError("上游已存在，切勿重复添加");
        }
        entity.setChannelState(1);
        GenericityUtil.setDate(entity);
        channelInfoDao.insert(entity);
        return setResultSuccess();
    }

    @Override
    public ResponseBase update(@RequestBody ChannelInfoEntity entity) {
        Integer id = entity.getId();
        if (id == null){
            return setResultError("上游id不能为空");
        }
        ChannelInfoEntity channelInfoEntity = channelInfoDao.selectById(id);
        if (channelInfoEntity == null){
            return setResultError("上游信息不存在，请确认信息是否正确");
        }
        entity.setGmtModified(new Date());
        channelInfoDao.updateById(entity);
        return setResultSuccess();
    }

    @Override
    public ResponseBase findList(@RequestBody ChannelInfoEntity entity) {
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        QueryWrapper<ChannelInfoEntity> wrapper = new QueryWrapper<>();
        String channelName = entity.getChannelName();
        if (StringUtils.isNoneBlank(channelName)) {
            wrapper.eq("channel_name", channelName);
        }
        Integer channelType = entity.getChannelType();
        if (channelType != null){
            wrapper.eq("channel_type",channelType);
        }
        Integer channelState = entity.getChannelState();
        if (channelState != null){
            wrapper.eq("channel_state",channelState);
        }
        wrapper.orderByDesc("setTime");
        List<ChannelInfoEntity> list = channelInfoDao.selectList(wrapper);
        PageInfo<ChannelInfoEntity> info = new PageInfo<>(list);
        return setResultSuccess(info, Constants.SUCCESS);
    }

    @Override
    public ResponseBase updateState(Integer id, Integer channelStatus) {
        ChannelInfoEntity channelInfoEntity = channelInfoDao.selectById(id);
        if (channelInfoEntity == null){
            return setResultError("上游信息不存在，请确认信息是否正确");
        }
        channelInfoEntity.setChannelState(channelStatus);
        channelInfoEntity.setGmtModified(new Date());
        channelInfoDao.updateById(channelInfoEntity);
        return setResultSuccess();
    }
}