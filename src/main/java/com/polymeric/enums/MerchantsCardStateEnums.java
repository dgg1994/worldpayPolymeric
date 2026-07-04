package com.polymeric.enums;

import com.polymeric.entity.system.DicEntity;

import java.util.ArrayList;
import java.util.List;

public enum MerchantsCardStateEnums {
	NORMAL(1, "正常"),
	DISABLE(2, "禁用");

	    private Integer index;

	    private String name;


	    private MerchantsCardStateEnums(Integer index, String name) {
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
	    	MerchantsCardStateEnums[] carTypeEnums = values();
	        for (MerchantsCardStateEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getIndex().equals(i)) {
	                return carTypeEnum.getName();
	            }
	        }
	        return null;
	    }

	    public static Integer getIndex(String index) {
	    	MerchantsCardStateEnums[] carTypeEnums = values();
	        for (MerchantsCardStateEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getName().equals(index)) {
	                return carTypeEnum.getIndex();
	            }
	        }
	        return null;
	    }
	    
	    public static List<DicEntity> getList() {
	    	MerchantsCardStateEnums[] typeEnums = values();
			List<DicEntity> list = new ArrayList<>();
			for (MerchantsCardStateEnums typeEnum : typeEnums) {
				DicEntity dicEntity = new DicEntity();
				dicEntity.setId(typeEnum.getIndex());
				dicEntity.setName(typeEnum.getName());
				list.add(dicEntity);
			}
			return list;
		}


}
