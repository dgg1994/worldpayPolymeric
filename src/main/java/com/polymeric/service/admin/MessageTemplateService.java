package com.polymeric.service.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polymeric.base.ResponseBase;
import com.polymeric.entity.template.MessageTemplateEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/messageTemplate")
@Api(value = "消息模板",tags = "消息模板")
public interface MessageTemplateService {
	
	@PostMapping("/add")
	@ApiOperation(value = "新增消息模板", notes = "新增消息模板", response = ResponseBase.class)
	ResponseBase add(MessageTemplateEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑消息模板", notes = "编辑消息模板", response = ResponseBase.class)
	ResponseBase update(MessageTemplateEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除消息模板", notes = "删除消息模板", response = ResponseBase.class)
	ResponseBase delete(Integer id);
	
	@GetMapping("/findCode")
	@ApiOperation(value = "根据code查询消息模板", notes = "根据code查询消息模板", response = ResponseBase.class)
	MessageTemplateEntity findCode(Integer code);
	
	@PostMapping("/findList")
	@ApiOperation(value = "根据code查询消息模板", notes = "根据code查询消息模板", response = ResponseBase.class)
	ResponseBase findList(MessageTemplateEntity entity);


}
