package com.polymeric.enums;

/**
 * @category 通知消息
 * @author Hlin
 *
 */
public enum MessageEnums {
	
	// 注册成功通知
	REGISTER_ZH(1004, "zh", "注册成功通知", "您的账号{}已注册成功"),
	// 登录成功通知
	LOGIN_ZH(1005, "zh", "登录成功通知", "您的账号 {} 检测到新的登录活动，登录设备：{},登录IP：{}。请确认是否为本人操作，如非本人操作，请及时修改密码以保障账户安全");

	private Integer index;

	private String language;

	private String title;

	private String msg;

	private MessageEnums(Integer index, String language, String title, String msg) {
		this.index = index;
		this.language = language;
		this.title = title;
		this.msg = msg;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
