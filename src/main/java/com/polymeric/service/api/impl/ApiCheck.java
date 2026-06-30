package com.polymeric.service.api.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.dao.merchants.MerchantsInfoDao;
import com.polymeric.dao.merchants.MerchantsIpDao;
import com.polymeric.dao.merchants.MerchantsKeyDao;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.entity.merchants.MerchantsIpEntity;
import com.polymeric.entity.merchants.MerchantsKeyEntity;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.enums.SignHeardEnums;
import com.polymeric.enums.UserStateEnums;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.IpUtil;
import com.polymeric.utils.sign.RsaVerifyUtil;

@RestController
@Transactional
@CrossOrigin
public class ApiCheck extends BaseApiService{
	
	@Autowired
	private MerchantsInfoDao merchantsInfoDao;
	
	@Autowired
	private MerchantsKeyDao merchantsKeyDao;
	
	@Autowired
	private MerchantsIpDao merchantsIpDao;
	
	@Autowired
	private IpUtil ipUtil;
	
	/**
	 * @category 前置校验（商户、白名单、时间戳超时、幂等、签名）
	 * @param request HttpServletRequest
	 * @param requestBody 请求体对象
	 * @return ResponseBase
	 */
	public <T> ResponseBase checkHeader(HttpServletRequest request, T requestBody) {
	    // 获取请求头appid
	    String appid = request.getHeader(SignHeardEnums.APPID.getName());
	    MerchantsInfoEntity infoEntity = merchantsInfoDao.findByAppId(appid);
	    if(infoEntity == null) {
	    	 return setResultError(ErrorCodeEnum.APPID_ERROR.getCode(), I18nUtil.getMessage("Appid_error"));
	    }
	    if(!UserStateEnums.NORMAL.getIndex().equals(infoEntity.getMerchantsStatus())) {
	    	return setResultError(ErrorCodeEnum.APPID_STATE_ERROR.getCode(), I18nUtil.getMessage("appid_state_error"));
	    }
	    // 查询appid对应商户公钥
	    MerchantsKeyEntity keyEntity = merchantsKeyDao.findAppId(appid);
	    if(keyEntity == null) {
	        return setResultError(ErrorCodeEnum.APPID_ERROR.getCode(), I18nUtil.getMessage("Appid_error"));
	    }
	    if(keyEntity.getPrivateKey() == null || keyEntity.getPrivateKey().isEmpty() 
	            || keyEntity.getPublicKey() == null || keyEntity.getPublicKey().isEmpty()) {
	        return setResultError(I18nUtil.getMessage("secretKey_error"));
	    }
	    // IP白名单控制
	    String ipAddress = ipUtil.getClientIp(request);
	    MerchantsIpEntity ipEntity = merchantsIpDao.findAppIdIp(appid, ipAddress);
	    if(ipEntity == null) {
	        return setResultError(ErrorCodeEnum.IP_ERROR.getCode(), I18nUtil.getMessage("ip_error"));
	    }
	    // 获取请求头时间戳
	    String timestamp = request.getHeader(SignHeardEnums.TIMESTAMP.getName());
	    // 校验到期时间
	    boolean timestampState = RsaVerifyUtil.validateTimestamp(timestamp);
	    if(!timestampState) {
	        return setResultError(ErrorCodeEnum.TIMESTAMP_ERROR.getCode(), I18nUtil.getMessage("timestamp_error"));
	    }
	    // 获取请求头随机数
	    String nonce = request.getHeader(SignHeardEnums.NONCE.getName());
	    // 校验随机字符，幂等处理
	    boolean nonceState = RsaVerifyUtil.validateNonce(appid, nonce);
	    if(!nonceState) {
	        return setResultError(ErrorCodeEnum.NONCE_ERROR.getCode(), I18nUtil.getMessage("nonce_error"));
	    }
	    // 获取请求头签名
	    String sign = request.getHeader(SignHeardEnums.SIGN.getName());
	    // 构建原始签名
	    String newSign = RsaVerifyUtil.buildSignString(appid, nonce, timestamp, JSON.toJSONString(requestBody));
	    // 签名校验
	    boolean signState = RsaVerifyUtil.verifySign(newSign, sign, keyEntity.getPublicKey());
	    if(!signState) {
	        return setResultError(ErrorCodeEnum.SIGN_ERROR.getCode(), I18nUtil.getMessage("sign_error"));
	    }
	    infoEntity.setMerchantsKey(keyEntity);
	    return setResultSuccess(infoEntity);
	}

}
