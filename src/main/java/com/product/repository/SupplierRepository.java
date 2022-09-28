package com.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.model.Suppliers;

@Repository
public interface SupplierRepository extends JpaRepository<Suppliers, Integer>{
	
	List<Suppliers> findByNameIgnoreCaseContaining(String name);
	
	

}
