package com.product.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.product.model.Products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSalesResponse {

	private Integer Id;
	private String name;
	@JsonProperty("quantity_available")
	private Integer quantityAvailable;
	@JsonProperty("create_at")
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	private SupplierResponse supplierId;
	private CategoryResponse categoryId;
	private List<String> sales;
	
	public static ProductSalesResponse of(Products products,List<String> sales) {
		return ProductSalesResponse
				.builder()
				.Id(products.getId())
				.name(products.getName())
				.quantityAvailable(products.getQuantityAvailable())
				.createdAt(products.getCreatedAt())
				.supplierId(SupplierResponse.of(products.getSuppliers()))
				.categoryId(CategoryResponse.of(products.getCategories()))
				.sales(sales)
				.build();
	}
}
