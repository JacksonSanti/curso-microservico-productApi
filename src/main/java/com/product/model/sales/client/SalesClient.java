package com.product.model.sales.client;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.product.model.sales.dto.SalesProductResponse;

@FeignClient(name="salesClientProducts",contextId="salesClient",url="${app.config.services.sales}")
public interface SalesClient {
	
		@GetMapping("products/{productId}")
		Optional<SalesProductResponse> findSalesByProductId(@PathVariable(value="productId") Integer productsId );
	
	
	
}
