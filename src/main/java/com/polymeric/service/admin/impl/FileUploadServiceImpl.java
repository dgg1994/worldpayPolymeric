package com.polymeric.service.admin.impl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.polymeric.aop.SysLogAnnotation;
import com.polymeric.base.BaseApiService;
import com.polymeric.base.ResponseBase;
import com.polymeric.enums.FilePathEnums;
import com.polymeric.service.admin.FileUploadService;
import com.polymeric.utils.I18nUtil;
import com.polymeric.utils.S3FileUploadUtil;

@RestController
@Transactional
@CrossOrigin
public class FileUploadServiceImpl extends BaseApiService implements FileUploadService{
	
	
	@Override
	@SysLogAnnotation(module = "通用文件上传", type = "post", remark = "上传文件")
	public ResponseBase uploadImage(MultipartFile file) {
		try {
			if(file != null) {
				String fileUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.RICHTEXT.getName());
				if(fileUrl != null && fileUrl.length() > 0) {
					return setResultSuccess(fileUrl, I18nUtil.getMessage("base_success"));
				}
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


}
