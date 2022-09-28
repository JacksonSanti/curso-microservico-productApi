package com.product.dto;

import org.springframework.beans.BeanUtils;

import com.product.model.Suppliers;

import lombok.Data;

@Data
public class SupplierResponse {

	private Integer Id;
	private String name;
	
	public static SupplierResponse of(Suppliers suppliers) {
		var response = new SupplierResponse();
		BeanUtils.copyProperties(suppliers, response);
		return response;
	}
	
}
