package com.product.dto;

import org.springframework.beans.BeanUtils;

import com.product.model.Categories;

import lombok.Data;

@Data
public class CategoryResponse {

	private Integer Id;
	private String description;
	
	public static CategoryResponse of(Categories categories) {
		var response = new CategoryResponse();
		BeanUtils.copyProperties(categories, response);
		return response;
	}

	
}
