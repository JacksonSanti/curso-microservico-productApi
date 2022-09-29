package com.product.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.config.Exception.SuccessResponse;
import com.product.config.Exception.ValidationException;
import com.product.dto.SupplierRequest;
import com.product.dto.SupplierResponse;
import com.product.model.Suppliers;
import com.product.repository.SupplierRepository;

@Service
public class SupplierService {

		@Autowired
		SupplierRepository supplierRepository;
		@Autowired
		ProductService productService;
		
		public SupplierResponse save(SupplierRequest request) {
			validateSupplierNameInforme(request);
			var supplier = supplierRepository.save(Suppliers.of(request));
			return SupplierResponse.of(supplier);
		}
		
		public Suppliers findById(Integer id) {
			validateInformedId(id);
			return supplierRepository
					.findById(id)
					.orElseThrow(()-> new ValidationException("There's no supplier for the given ID"));
		}
		
		
		private void validateSupplierNameInforme(SupplierRequest request) {
			if (isEmpty(request.getName())) {
				throw new ValidationException("The Suppliers' description was not informed.");
			}
		}
		
		public List<SupplierResponse> findAll() {
			return supplierRepository
					.findAll()
					.stream()
					.map(SupplierResponse::of)
					.collect(Collectors.toList());
		}
		
		public List<SupplierResponse> findByName(String name) {
			if(isEmpty(name)) {
				throw new ValidationException("The supplier name must be informed.");
			}
			
			return supplierRepository
					.findByNameIgnoreCaseContaining(name)
					.stream()
					.map(SupplierResponse::of)
					.collect(Collectors.toList());
		}
		
		public SupplierResponse findByIdResponse(Integer id) {
			validateInformedId(id);
			return SupplierResponse.of(findById(id));
		}
		
		public SuccessResponse delete(Integer id) {
			validateInformedId(id);
			if(productService.existsBySuppliers(id)) {
				throw new ValidationException("You can't delete this supplier becausa it's already defined by a product.");
			}
			supplierRepository.deleteById(id);
			return SuccessResponse.create("The supplier was deleted.");
		}
		
		private void validateInformedId(Integer id) {
			if(isEmpty(id)) {
				throw new ValidationException("The supplier ID must be informed.");
			}
		}
		
		public SupplierResponse update(SupplierRequest request, Integer id) {
			validateSupplierNameInforme(request);
			validateInformedId(id);
			var supplier = Suppliers.of(request);
			supplier.setId(id);
			supplierRepository.save(supplier);
			return SupplierResponse.of(supplier);
		}
		
	
}
