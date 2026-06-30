package com.polymeric.enums;

public enum ImageTypeEnums {
	APK(1, "apk/"),//app安装包
	TOUXIANG(2, "touxiang/"),//头像
	WORK(3, "work/"),//任务附件
	WIKI(4, "wiki/"),//知识库
	AFFICHE(5, "affiche/"),
	MESSAGE(6, "message/"),
	FEEDBACK(7, "feedback/");//消息通知
	
	private Integer index;

	private String name;


	private ImageTypeEnums(Integer index, String name) {
	    this.index = index;
	    this.name = name;
	}


	public Integer getIndex() {
	    return index;
	}


	public void setIndex(Integer index) {
	   this.index = index;
	}


	public String getName() {
	     return name;
	}


	public void setName(String name) {
	    this.name = name;
	}

}
