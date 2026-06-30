package com.polymeric.response.pub;

import com.github.pagehelper.PageInfo;

import lombok.Data;

@Data
public class PublicRes<T> {

	private PageInfo<T> info;
	
	private Object data;
}
