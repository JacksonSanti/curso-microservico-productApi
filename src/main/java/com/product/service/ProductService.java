package com.product.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.configException.SuccessResponse;
import com.product.configException.ValidationException;
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.model.Products;
import com.product.repository.ProductRepository;

@Service
public class ProductService {
	
		private static final Integer ZERO = 0;
	
	

		@Autowired
		ProductRepository productRepository;
		
		@Autowired
		SupplierService supplierService;
		
		@Autowired
		CategoryService categoryService;
		
	
		public ProductResponse save(ProductRequest request) {
			validateProductNameInforme(request);
			validateCategoryAndSupplierNameInforme(request);
			var category = categoryService.findById(request.getCategoryID());
			var supplier = supplierService.findById(request.getSupplierID());
			var product = productRepository.save(Products.of(request,supplier,category));
			return ProductResponse.of(product);
		}
	
		private void validateProductNameInforme(ProductRequest request) {
			if (isEmpty(request.getName())) {
				throw new ValidationException("The product's name description was not informed.");
			}
			if (isEmpty(request.getQuantityAvailable())) {
				throw new ValidationException("The product's quantity description was not informed.");
			}
			if (request.getQuantityAvailable() <= ZERO) {
				throw new ValidationException("The product's should not be less or equal to zero.");
			}
			
			
		}
		
		private void validateCategoryAndSupplierNameInforme(ProductRequest request) {
			if (isEmpty(request.getCategoryID())) {
				throw new ValidationException("The category ID was not informed.");
			}
			if (isEmpty(request.getSupplierID())) {
				throw new ValidationException("The supplier ID was not informed.");
			}
		}
		
		public Products findById(Integer id) {
			validateInformedId(id);
			return productRepository
					.findById(id)
					.orElseThrow(()-> new ValidationException("There's no products for the given ID"));
		}
		
		public List<ProductResponse> findAll() {
			return productRepository
					.findAll()
					.stream()
					.map(ProductResponse::of)
					.collect(Collectors.toList());
		}
		
		public List<ProductResponse> findByName(String name) {
			if(isEmpty(name)) {
				throw new ValidationException("The product name must be informed.");
			}
			
			return productRepository
					.findByNameIgnoreCaseContaining(name)
					.stream()
					.map(ProductResponse::of)
					.collect(Collectors.toList());
		}
		
		public List<ProductResponse> findBySupplierId(Integer suppliers) {
			validateInformedId(suppliers);
			return productRepository
					.findBySuppliers(suppliers)
					.stream()
					.map(ProductResponse::of)
					.collect(Collectors.toList());
		}
		
		public List<ProductResponse> findByCategoryId(Integer categories) {
			if(isEmpty(categories)) {
				throw new ValidationException("The product's category ID must be informed.");
			}
			
			return productRepository
					.findByCategories(categories)
					.stream()
					.map(ProductResponse::of)
					.collect(Collectors.toList());
		}
	
		
		public ProductResponse findByIdResponse(Integer id) {
			validateInformedId(id);
			return ProductResponse.of(findById(id));
		}
		
		public Boolean existsByCategories (Integer id) {
			return productRepository 
					.existsByCategoriesId(id);	
		}
		
		public Boolean existsBySuppliers (Integer id) {
			return productRepository 
					.existsBySuppliersId(id);	
		}
		
		public SuccessResponse delete(Integer id) {
			validateInformedId(id);
			productRepository.deleteById(id);
			return SuccessResponse.create("The product was deleted.");
		}
		
		private void validateInformedId(Integer id) {
			if(isEmpty(id)) {
				throw new ValidationException("The product ID must be informed.");
			}
		}
		
		public ProductResponse update(ProductRequest request,Integer id) {
			validateProductNameInforme(request);
			validateInformedId(id);
			validateCategoryAndSupplierNameInforme(request);
			var category = categoryService.findById(request.getCategoryID());
			var supplier = supplierService.findById(request.getSupplierID());
			var product = Products.of(request,supplier,category);
			product.setId(id);
			productRepository.save(product);
			return ProductResponse.of(product);
		}
		
		
}
