package com.product.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.config.Exception.SuccessResponse;
import com.product.config.Exception.ValidationException;
import com.product.dto.ProductCheckStockRequest;
import com.product.dto.ProductQuantity;
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.dto.ProductSalesResponse;
import com.product.dto.ProductStock;
import com.product.model.Products;
import com.product.model.sales.client.SalesClient;
import com.product.model.sales.dto.SalesConfirmationdto;
import com.product.model.sales.enums.SalesStatus;
import com.product.model.sales.rabbitmq.SalesConfirmationSender;
import com.product.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ProductService {
	
		private static final Integer ZERO = 0;
	
	

		@Autowired
		ProductRepository productRepository;
		
		@Autowired
		SupplierService supplierService;
		
		@Autowired
		CategoryService categoryService;
		
		@Autowired
		SalesConfirmationSender salesConfirmationSender;
		
		@Autowired
		SalesClient salesClient;
	
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
		
		
		@Transactional
		private void validateStockUpdateData(ProductStock productStock) {
			if(isEmpty(productStock) || isEmpty(productStock.getSalesId())) {
				throw new ValidationException("The product data or sales ID must be informed.");
			}
			if(isEmpty(productStock.getProducts())){
				throw new ValidationException("The sale's product must be informed.");
			}
			productStock
				.getProducts()
				.forEach(salesProduct -> {
					if(isEmpty(salesProduct.getQuantity()) || isEmpty(salesProduct.getProductId())){
						throw new ValidationException("The productID and the quantity must be informed.");
					}
				});
		}
		
		@Transactional
		private void updateStock(ProductStock productStock) {
			var productForUpdate = new ArrayList<Products>();		
			productStock
			.getProducts()
			.forEach(salesProduct -> {
				var existingProduct = findById(salesProduct.getProductId());
				validateQuantityInStock(salesProduct,existingProduct);
				existingProduct.updateStock(salesProduct.getQuantity());
				productForUpdate.add(existingProduct);
			});
			if(!isEmpty(productForUpdate)) {
				productRepository.saveAll(productForUpdate);
				var approvedMessage = new SalesConfirmationdto(productStock.getSalesId(),SalesStatus.APPROVED);
				salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
			}
			
		}
		
		private void validateQuantityInStock(ProductQuantity salesProduct, Products existingProduct) {
			if(salesProduct.getQuantity() > existingProduct.getQuantityAvailable()) {
				throw new ValidationException(String.format("The product %s is out stock", existingProduct.getId()));
			}
		}
		
		public void updateProductStock(ProductStock productStock) {
			try {
				validateStockUpdateData(productStock);
				updateStock(productStock);
			} catch (Exception e) {
				log.error("Error while trying to update stock for message with error: {}", e.getMessage(),e);
				var rejectMessage = new SalesConfirmationdto(productStock.getSalesId(),SalesStatus.REJECTED);
				salesConfirmationSender.sendSalesConfirmationMessage(rejectMessage);
			}
		}
			
		public ProductSalesResponse findProductSales(Integer productId) {
			var product = findById(productId);
			try {
				var sales = salesClient
						.findSalesByProductId(product.getId())
						.orElseThrow(()-> new ValidationException("The sales was not found by this product."));
				return ProductSalesResponse.of(product, sales.getSalesIds());
				
			} catch (Exception e) {
				throw new ValidationException("There was an error trying to get the product' sales.");
			}
		}
		
		public SuccessResponse checkProductsStock(ProductCheckStockRequest request) {
			if(isEmpty(request) || isEmpty(request.getProducts())) {
				throw new ValidationException("The request data and product must no be null.");
			}
			request
				.getProducts()
				.forEach(this::validateStock);
			return SuccessResponse.create("The stock is ok!");
		}
		
		private void validateStock(ProductQuantity productQuantity) {
			if(isEmpty(productQuantity.getProductId()) || isEmpty(productQuantity.getQuantity())) {
				throw new ValidationException("Product Id and quantity must be informed.");
			}
			var product = findById(productQuantity.getProductId());
			if(productQuantity.getQuantity() > product.getQuantityAvailable()) {
				throw new ValidationException(String.format("The product %s is out of stock.",product.getId()));
			}
		
		}
		
}
