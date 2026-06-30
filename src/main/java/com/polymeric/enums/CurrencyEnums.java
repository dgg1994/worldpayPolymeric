package com.polymeric.enums;

import java.util.ArrayList;
import java.util.List;

import com.polymeric.entity.system.DicEntity;


/**
 * @category 币种
 * @author Hlin
 *
 */
public enum CurrencyEnums {
	CNY(1, "CNY", "CNY", "人民币"),
	USD(2, "USD", "USD", "美元"),
	HKD(3, "HKD", "HKD", "港币"),
	TRY(4, "TRY", "TRY", "土耳其里拉"),
	KRW(5, "KRW", "KRW", "韩元"),
	JPY(6, "JPY", "JPY", "日元");
	

	private Integer index;

	private String name;

	private String value;
	
	private String lable;

	private CurrencyEnums(Integer index, String name, String value,String lable) {
		this.index = index;
		this.name = name;
		this.value = value;
		this.lable = lable;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public static String getName(int i) {
		CurrencyEnums[] typeEnums = values();
		for (CurrencyEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getName();
			}
		}
		return null;
	}
	
	public static String getLable(String name) {
		CurrencyEnums[] typeEnums = values();
		for (CurrencyEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum.getLable();
			}
		}
		return null;
	}
	
	public static List<DicEntity> getList() {
		CurrencyEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (CurrencyEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			dicEntity.setValue(typeEnum.getValue());
			list.add(dicEntity);
		}
		return list;
	}
	
	public static List<DicEntity> getLableList() {
		CurrencyEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (CurrencyEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			dicEntity.setValue(typeEnum.getLable());
			list.add(dicEntity);
		}
		return list;
	}
	
	public static List<String> getAll() {
		CurrencyEnums[] typeEnums = values();
		List<String> list = new ArrayList<>();
		for (CurrencyEnums typeEnum : typeEnums) {
			list.add(typeEnum.getName());
		}
		return list;
	}

}
