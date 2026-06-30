package com.polymeric.service.api.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.config.channel.PoloConfig;
import com.polymeric.config.channel.PoloMethods;
import com.polymeric.config.channel.UnifiedConfig;
import com.polymeric.constants.Constants;
import com.polymeric.entity.merchants.MerchantsInfoEntity;
import com.polymeric.enums.ChannelCodeEnums;
import com.polymeric.enums.ErrorCodeEnum;
import com.polymeric.service.api.ApiFileUploadService;
import com.polymeric.utils.FileUploadUtil;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.sign.ApiPoloUtil;


@RestController
@Transactional
@CrossOrigin
public class ApiFileUploadServiceImpl extends BaseApiService implements ApiFileUploadService{

	@Override
	public ResponseBase upload(HttpServletRequest request, MultipartFile idCard) {
		try {
			ResponseBase base = ApiCheck.checkHeader(request, null);
			if(!Constants.HTTP_RES_CODE_200.equals(base.getCode())) {
				return base;
			}
			MerchantsInfoEntity infoEntity = JSONObject.parseObject(JSON.toJSONString(base.getData()), MerchantsInfoEntity.class);
			if(ChannelCodeEnums.POLO.getCode().equals(infoEntity.getChannelCode())) {
				return this.poloFileUpload(infoEntity,idCard);
			}else {
				return setResultError(ErrorCodeEnum.CHANNEL_NULL.getCode(), I18nUtil.getMessage("channel_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * @category polo 单个文件上传
	 * @param infoEntity
	 * @param file 
	 * @return
	 */
	public ResponseBase poloFileUpload(MerchantsInfoEntity infoEntity, MultipartFile idCard) {
		try {
			// 获取上游配置
			UnifiedConfig config = ApiCheck.getConfig(
					infoEntity.getChannelData(),
					PoloConfig.API_URL, 
					PoloConfig.APP_ID,
					PoloConfig.RSA_PRIVATE_KEY, 
					PoloConfig.AES_KEY, 
					PoloMethods.UPLOAD_FILE);
			if (infoEntity.getMerchantsUserData() == null) {
				return setResultError(ErrorCodeEnum.UID_NULL.getCode(), I18nUtil.getMessage("uid_null"));
			}
			if(idCard == null) {
				return setResultError(I18nUtil.getMessage("file_check"));
			}
			// 获取文件名
	        String fileName = idCard.getOriginalFilename();
	        if (fileName == null) {
	            return setResultError(I18nUtil.getMessage("file_name_check"));
	        }
	        // 转小写，防止大小写问题
	        String lowerName = fileName.toLowerCase();
	        // 允许的文件后缀
	        List<String> allowedExt = Arrays.asList(".png", ".pdf", ".jpg", ".jpeg");
	        // 检查文件扩展名
	        boolean valid = allowedExt.stream().anyMatch(lowerName::endsWith);
	        if (!valid) {
	            return setResultError(I18nUtil.getMessage("file_type_check"));
	        }
	        byte[] fileBytes = idCard.getBytes();
            File tempFile = FileUploadUtil.createTempFileFromBytes(fileBytes, idCard.getOriginalFilename());
	        ResponseBase base = ApiPoloUtil.postFormFile(infoEntity.getMerchantsUserData().getApiUid(), Constants.ID_CARD, tempFile, config);
	        return base;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
