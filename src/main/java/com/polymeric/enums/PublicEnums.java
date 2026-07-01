package com.polymeric.enums;

public enum PublicEnums {
	ZERO(0, "0"),
	ONE(1, "1"),//上架中
	TOW(2, "2"),//下架中
	THREE(3, "3"),
	FOUR(4, "4"),
	ONE_HUNDRED(100, "100");

	    private Integer index;

	    private String name;


	    private PublicEnums(Integer index, String name) {
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
