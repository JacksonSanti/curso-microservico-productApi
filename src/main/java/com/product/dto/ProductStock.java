package com.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStock {

	private String salesId;
	private List<ProductQuantity> products;
	
	
	
}
