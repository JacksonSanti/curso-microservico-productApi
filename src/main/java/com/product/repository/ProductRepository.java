package com.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.model.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer>{

	List<Products> findByNameIgnoreCaseContaining(String name);
	
	List<Products> findByCategories(Integer categories);

	List<Products> findBySuppliers(Integer suppliers);
	
	Boolean existsByCategoriesId(Integer id);
	
	Boolean existsBySuppliersId(Integer id);
	
}
