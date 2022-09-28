package com.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.configException.SuccessResponse;
import com.product.dto.CategoryRequest;
import com.product.dto.CategoryResponse;
import com.product.service.CategoryService;

@RestController
@RequestMapping("/api/category/")
public class CategoryController {

	
	@Autowired
	CategoryService categoryService;
	
	
	
	@PostMapping()
	public CategoryResponse save(@RequestBody CategoryRequest request) {
		return categoryService.save(request);
	}
	
	@GetMapping()
	public List<CategoryResponse> findAll(){
		return categoryService.findAll();
	}
	
	@GetMapping("/{id}")
	public CategoryResponse findById(@PathVariable(value="id") Integer id){
		return categoryService.findByIdResponse(id);
	}
	
	@GetMapping("description/{description}")
	public List<CategoryResponse> findByDescription(@PathVariable String description){
		return categoryService.findByDescription(description);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponse deleteById(@PathVariable(value="id") Integer id){
		return categoryService.delete(id);
	}
	
	@PutMapping("/{id}")
	public CategoryResponse update (@RequestBody CategoryRequest request, @PathVariable(value="id") Integer id){
		return categoryService.update(request, id);
	}
}
