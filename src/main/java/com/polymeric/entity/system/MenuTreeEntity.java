package com.polymeric.entity.system;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class MenuTreeEntity {
	
	/** 节点ID */
    private Integer id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuTreeEntity> children;

	public MenuTreeEntity(Integer id, String label, List<MenuTreeEntity> children) {
		super();
		this.id = id;
		this.label = label;
		this.children = children;
	}

	public MenuTreeEntity() {
		super();
	}
    
    

}
