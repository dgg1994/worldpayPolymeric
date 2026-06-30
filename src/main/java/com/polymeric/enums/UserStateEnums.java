package com.polymeric.enums;

import java.util.ArrayList;
import java.util.List;

import com.polymeric.entity.system.DicEntity;

public enum UserStateEnums {
	NORMAL(1, "正常"),
	LOGOUT(0, "注销"),
	DISABLE(2, "禁用");

	    private Integer index;

	    private String name;


	    private UserStateEnums(Integer index, String name) {
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

	    public static String getValue(int i) {
	    	UserStateEnums[] carTypeEnums = values();
	        for (UserStateEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getIndex().equals(i)) {
	                return carTypeEnum.getName();
	            }
	        }
	        return null;
	    }

	    public static Integer getIndex(String index) {
	    	UserStateEnums[] carTypeEnums = values();
	        for (UserStateEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getName().equals(index)) {
	                return carTypeEnum.getIndex();
	            }
	        }
	        return null;
	    }
	    
	    public static List<DicEntity> getList() {
	    	UserStateEnums[] typeEnums = values();
			List<DicEntity> list = new ArrayList<>();
			for (UserStateEnums typeEnum : typeEnums) {
				DicEntity dicEntity = new DicEntity();
				dicEntity.setId(typeEnum.getIndex());
				dicEntity.setName(typeEnum.getName());
				list.add(dicEntity);
			}
			return list;
		}


}
