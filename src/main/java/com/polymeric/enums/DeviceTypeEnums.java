package com.polymeric.enums;

import java.util.ArrayList;
import java.util.List;

import com.polymeric.entity.system.DicEntity;


/**
 * @category 设备类型
 * @author Hlin
 *
 */
public enum DeviceTypeEnums {
	CLIENT_ANDROID(1, "Android"),
	CLIENT_IOS(2, "iOS"),
	CLIENT_WEB(3, "Web"),
	CLIENT_IPAD(4, "iPad"),
	CLIENT_DESKTOP(5, "Desktop"),
	CLIENT_EXTENSION(6, "Extension");
	
	private Integer index;

	private String name;


	private DeviceTypeEnums(Integer index, String name) {
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
	
	public static List<DicEntity> getList() {
		DeviceTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (DeviceTypeEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
