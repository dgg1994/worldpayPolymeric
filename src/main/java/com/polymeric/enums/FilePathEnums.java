package com.polymeric.enums;


public enum FilePathEnums {
	PRODUCT(1, "product/","product","理财产品"),
	RICHTEXT(2, "richtext/","richtext","富文本"),
	KYC(3, "kyc/","kyc","实名认证"),
	APK(4, "apk/","apk","安装包"),
	CARD(4, "card/","card","银行卡"),
	HELP(6, "help/","help","帮助中心"),
	CONFIG(6, "config/","config","配置中");

	private Integer index;

	private String name;
	
	private String databaseName;
	
	private String value;

	private FilePathEnums(Integer index, String name, String databaseName, String value) {
		this.index = index;
		this.name = name;
		this.databaseName = databaseName;
		this.value = value;
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
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getName(int i) {
		FilePathEnums[] carTypeEnums = values();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getName();
			}
		}
		return null;
	}
	
	public static String getValue(int i) {
		FilePathEnums[] carTypeEnums = values();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getValue();
			}
		}
		return null;
	}

	public static Integer getIndex(String index) {
		FilePathEnums[] carTypeEnums = values();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getName().equals(index)) {
				return carTypeEnum.getIndex();
			}
		}
		return null;
	}
	
	public static String getDatabaseName(int i) {
		FilePathEnums[] carTypeEnums = values();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getDatabaseName();
			}
		}
		return null;
	}

}
