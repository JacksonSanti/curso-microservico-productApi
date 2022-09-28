package com.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.model.Categories;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer>{

	
	List<Categories> findByDescriptionIgnoreCaseContaining(String description);
	
	
}
