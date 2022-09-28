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
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.service.ProductService;

@RestController
@RequestMapping("/api/product/")
public class ProductController {

	
	@Autowired
	ProductService productService;
	
	
	@PostMapping()
	public ProductResponse save(@RequestBody ProductRequest request) {
		return productService.save(request);
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
	public List<ProductResponse> findAll(){
		return productService.findAll();
	}
	
	@GetMapping("/{id}")
	public ProductResponse findById(@PathVariable(value="id") Integer id){
		return productService.findByIdResponse(id);
	}
	
	@GetMapping("name/{name}")
	public List<ProductResponse> findByName(@PathVariable String name){
		return productService.findByName(name);
	}
	
	@GetMapping("category/{categoryId}")
	public List<ProductResponse> findByCategoryId(@PathVariable Integer categories){
		return productService.findByCategoryId(categories);
	}
	
	@GetMapping("supplier/{supplierId}")
	public List<ProductResponse> findBySupplierId(@PathVariable Integer suppliers){
		return productService.findBySupplierId(suppliers);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponse deleteById(@PathVariable(value="id") Integer id){
		return productService.delete(id);
	}
	
	@PutMapping("/{id}")
	public ProductResponse update (@RequestBody ProductRequest request, @PathVariable(value="id") Integer id){
		return productService.update(request, id);
	}
	
	
	
}
