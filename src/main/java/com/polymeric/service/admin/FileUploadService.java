package com.polymeric.service.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.polymeric.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/wiki")
@Api(value = "通用文件上传",tags = "通用文件上传")
public interface FileUploadService {
	
	@PostMapping("/uploadImage")
	@ApiOperation(value = "上传文件",notes = "上传文件",response = ResponseBase.class)
	ResponseBase uploadImage(MultipartFile file);
	
}
