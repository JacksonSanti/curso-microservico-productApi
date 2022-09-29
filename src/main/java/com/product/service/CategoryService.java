package com.product.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.config.Exception.SuccessResponse;
import com.product.config.Exception.ValidationException;
import com.product.dto.CategoryRequest;
import com.product.dto.CategoryResponse;
import com.product.model.Categories;
import com.product.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ProductService productService;
	
	public CategoryResponse findByIdResponse(Integer id) {
		return CategoryResponse.of(findById(id));
	}
	
	
	public List<CategoryResponse> findAll() {
		return categoryRepository
				.findAll()
				.stream()
				.map(CategoryResponse::of)
				.collect(Collectors.toList());
	}
	
	public List<CategoryResponse> findByDescription(String description) {
		if(isEmpty(description)) {
			throw new ValidationException("The category description must be informed.");
		}
		
		return categoryRepository
				.findByDescriptionIgnoreCaseContaining(description)
				.stream()
				.map(CategoryResponse::of)
				.collect(Collectors.toList());
	}
	
	public CategoryResponse save(CategoryRequest request) {
		validateCategoryNameInforme(request);
		var category = categoryRepository.save(Categories.of(request));
		return CategoryResponse.of(category);
	}
	
	public Categories findById(Integer id) {
		validateInformedId(id);
		return categoryRepository
				.findById(id)
				.orElseThrow(()-> new ValidationException("There's no Category for the given ID"));
	}
	
	private void validateCategoryNameInforme(CategoryRequest request) {
		if (isEmpty(request.getDescription())) {
			throw new ValidationException("The category description was not informed.");
		}
	}
	
	public SuccessResponse delete(Integer id) {
		validateInformedId(id);
		if(productService.existsByCategories(id)) {
			throw new ValidationException("You can't delete this supplier becausa it's already defined by a product.");
		}
		categoryRepository.deleteById(id);
		return SuccessResponse.create("The category was deleted.");
	}
	/*
	public SuccessResponse delete(Integer id) {
		validateInformedId(id);
		categoryRepository.deleteById(id);
		return SuccessResponse.create("The category was deleted.");
	}*/
	
	private void validateInformedId(Integer id) {
		if(isEmpty(id)) {
			throw new ValidationException("The category ID must be informed.");
		}
	}
	
	
	public CategoryResponse update(CategoryRequest request, Integer id) {
		validateCategoryNameInforme(request);
		validateInformedId(id);
		var category = Categories.of(request);
		category.setId(id);
		categoryRepository.save(category);
		return CategoryResponse.of(category);
	}
	
	
	
}
