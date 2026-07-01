package com.polymeric.query.api;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @category 统一注册参数
 * @author Hlin
 *
 */
@Data
public class ApiRegisterQuery {
	
	@NotBlank(message = "邮箱不能为空")
	private String email;
	
	private String tel;

}
