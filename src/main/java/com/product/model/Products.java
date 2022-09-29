package com.product.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.product.dto.ProductRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name= "PRODUCT")
public class Products {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name= "NAME", nullable= false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="FK_SUPPLIER",nullable=false)
	private Suppliers suppliers;
	
	@ManyToOne
	@JoinColumn(name="FK_CATEGORY",nullable=false)
	private Categories categories;
	
	@Column(name= "QUANTITY_AVAILABLE", nullable= false)
	private Integer quantityAvailable;
	
	@Column(name= "CREATED_AT", nullable= false,updatable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}
	
	
	
	public static Products of(ProductRequest request,Suppliers supplier,Categories category) {
		return Products
			.builder()
			.name(request.getName())
			.quantityAvailable(request.getQuantityAvailable())
			.categories(category)
			.suppliers(supplier)
			.build();
	}
	
	public void updateStock(Integer quantity) {
		quantityAvailable =- quantity;
	}
	
}
