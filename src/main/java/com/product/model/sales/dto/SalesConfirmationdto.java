package com.product.model.sales.dto;

import com.product.model.sales.enums.SalesStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesConfirmationdto {

	private String salesId;
	private SalesStatus salesStatus;
	
	
}
