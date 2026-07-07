package com.polymeric.enums;


import com.polymeric.entity.system.DicEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @category kyc认证状态
 * @author Hlin
 *
 */
public enum KycStateEnums {
	WAIT_APPROVE(1, "待认证"),
	PROCESS_APPROVE(2, "认证中"),
	SUCCESS_APPROVE(3, "认证成功"),
	ERROR_APPROVE(4, "认证失败");
	
	private Integer index;

	private String name;


	private KycStateEnums(Integer index, String name) {
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

	public static String getName(int i) {
		KycStateEnums[] kycStateEnums = values();
		for (KycStateEnums ky : kycStateEnums) {
			if (ky.getIndex().equals(i)) {
				return ky.getName();
			}
		}
		return null;
	}

	public static List<DicEntity> getList() {
		KycStateEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (KycStateEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}
}
