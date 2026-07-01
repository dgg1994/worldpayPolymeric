package com.polymeric.service.admin.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.channel.PoloConfig;
import com.polymeric.config.channel.PoloMethods;
import com.polymeric.config.channel.UnifiedConfig;
import com.polymeric.constants.Constants;
import com.polymeric.dao.channel.ChannelCardDao;
import com.polymeric.dao.channel.ChannelInfoDao;
import com.polymeric.entity.channel.ChannelCardEntity;
import com.polymeric.entity.channel.ChannelInfoEntity;
import com.polymeric.enums.PublicEnums;
import com.polymeric.response.polo.MerchantBankcardRes;
import com.polymeric.service.admin.ChannelCardService;
import com.polymeric.service.api.impl.ApiCheck;
import com.polymeric.utils.GenericityUtil;
import com.polymeric.utils.sign.ApiPoloUtil;

@RestController
@Transactional
@CrossOrigin
public class ChannelCardServiceImpl extends BaseApiService implements ChannelCardService{
	
	@Autowired
	private ChannelInfoDao channelInfoDao;
	
	@Autowired
	private ChannelCardDao channelCardDao;

	@Override
	public ResponseBase pull(Integer id) {
		try {
			ChannelInfoEntity channelInfoEntity = channelInfoDao.selectById(id);
			if(channelInfoEntity == null) {
				return setResultError(Constants.ERROR);				
			}
			//获取上游配置
			UnifiedConfig config= ApiCheck.getConfig(
					channelInfoEntity, 
					PoloConfig.API_URL,
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY,
					PoloConfig.AES_KEY,
					PoloMethods.MERCHANT_TEMPLATE_LIST);
			//调用三方接口
			ResponseBase base = ApiPoloUtil.postData(null, null, null, config);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			List<MerchantBankcardRes> list = JSONArray.parseArray(JSON.toJSONString(base.getData()), MerchantBankcardRes.class);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ChannelCardEntity entity = new ChannelCardEntity();
					BeanUtils.copyProperties(list.get(i), entity);
					entity.setId(null);
					entity.setCardTitle(list.get(i).getTitle());
					entity.setCardId(list.get(i).getId());
					entity.setChannelCode(channelInfoEntity.getChannelCode());
					entity.setChannelId(channelInfoEntity.getId());
					entity.setCardState(PublicEnums.ONE.getIndex());
					GenericityUtil.setDate(list.get(i));
					channelCardDao.insert(entity);
				}
			}
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(@RequestBody ChannelCardEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		QueryWrapper<ChannelCardEntity> wrapper = new QueryWrapper<>();
		Integer channelId = entity.getChannelId();
		if (channelId != null) {
			wrapper.eq("channel_id", channelId);
		}
		String cardTitle = entity.getCardTitle();
		if (StringUtils.isNoneBlank(cardTitle)) {
			wrapper.eq("card_title", cardTitle);
		}
		String bankCardNature = entity.getBankCardNature();
		if (StringUtils.isNoneBlank(bankCardNature)) {
			wrapper.eq("bank_card_nature", bankCardNature);
		}
		Integer cardState = entity.getCardState();
		if (cardState != null){
			wrapper.eq("card_state",cardState);
		}
		List<ChannelCardEntity> list = channelCardDao.selectList(wrapper);
		PageInfo<ChannelCardEntity> info = new PageInfo<>(list);
		return setResultSuccess(info, Constants.SUCCESS);
	}

	@Override
	public ResponseBase update(@RequestBody ChannelCardEntity entity) {
		Integer id = entity.getId();
		if (id == null){
			return setResultError("未传入产品id");
		}
		ChannelCardEntity channelCardEntity = channelCardDao.selectById(id);
		if (channelCardEntity == null){
			return setResultError("产品信息错误");
		}
		channelCardDao.updateById(entity);
		return setResultSuccess();
	}

	@Override
	public ResponseBase updateState(Integer id, Integer cardStatus) {
		ChannelCardEntity channelCardEntity = channelCardDao.selectById(id);
		if (channelCardEntity == null){
			return setResultError("产品信息错误");
		}
		channelCardEntity.setCardState(cardStatus);
		channelCardDao.updateById(channelCardEntity);
		return setResultSuccess();
	}

}
