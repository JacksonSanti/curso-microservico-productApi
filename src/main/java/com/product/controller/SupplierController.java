package com.product.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.configException.SuccessResponse;
import com.product.dto.SupplierRequest;
import com.product.dto.SupplierResponse;
import com.product.service.SupplierService;

@RestController
@RequestMapping("/api/supplier/")
public class SupplierController {

	
	@Autowired
	SupplierService supplierService;
	
	
	@PostMapping()
	public SupplierResponse save(@RequestBody SupplierRequest request) {
		return supplierService.save(request);
	}
	
	@GetMapping("status/")
	public ResponseEntity<HashMap<String, Object>> getProduct(){
		var response = new HashMap<String, Object>();
		
		response.put("service","Product_api");
		response.put("status","up");
		response.put("HttpStatus",HttpStatus.OK.value());
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping()
	public List<SupplierResponse> findAll(){
		return supplierService.findAll();
	}
	
	@GetMapping("/{id}")
	public SupplierResponse findById(@PathVariable(value="id") Integer id){
		return supplierService.findByIdResponse(id);
	}
	
	@GetMapping("name/{name}")
	public List<SupplierResponse> findByName(@PathVariable String name){
		return supplierService.findByName(name);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponse deleteById(@PathVariable(value="id") Integer id){
		return supplierService.delete(id);
	}
	
	@PutMapping("/{id}")
	public SupplierResponse update (@RequestBody SupplierRequest request, @PathVariable(value="id") Integer id){
		return supplierService.update(request, id);
	}
	
}
