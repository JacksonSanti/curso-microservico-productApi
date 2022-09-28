package com.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.product.dto.SupplierRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "SUPPLIER")
public class Suppliers {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(name= "NAME", nullable= false)
	private String name;
	
	public static Suppliers of(SupplierRequest request) {
		var response = new Suppliers();
		BeanUtils.copyProperties(request, response);
		return response;
	}

}
