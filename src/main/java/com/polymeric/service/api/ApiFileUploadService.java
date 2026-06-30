package com.polymeric.service.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.polymeric.base.ResponseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/file")
@Api(value = "文件上传",tags = "用户管理")
public interface ApiFileUploadService {
	
	@PostMapping("/upload")
	@ApiOperation(value = "文件上传", notes = "文件上传", response = ResponseBase.class)
	ResponseBase upload(HttpServletRequest request,MultipartFile idCard);

}
