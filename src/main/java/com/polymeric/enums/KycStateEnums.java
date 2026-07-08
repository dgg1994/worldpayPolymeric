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
	WAIT_APPROVE(1, "待认证","waiting"),
	PROCESS_APPROVE(2, "认证中","wait_audit"),
	SUCCESS_APPROVE(3, "认证成功","success"),
	ERROR_APPROVE(4, "认证失败","fail");
	
	private Integer index;

	private String name;
	
	private String lable;


	private KycStateEnums(Integer index, String name, String lable) {
	    this.index = index;
	    this.name = name;
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

	public String getLable() {
		return lable;
	}


	public void setLable(String lable) {
		this.lable = lable;
	}

}
