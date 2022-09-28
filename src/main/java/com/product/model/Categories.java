package com.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.product.dto.CategoryRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "CATEGORY")
public class Categories {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name= "DESCRIPTION",nullable= false)
	private String description;
	
	public static Categories of(CategoryRequest request) {
		var categories = new Categories();
		BeanUtils.copyProperties(request, categories);
		return categories;
	}

}
