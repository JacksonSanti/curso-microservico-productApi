package com.product.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.product.dto.ProductStock;
import com.product.service.ProductService;

@Component
public class ProductStockListener {

	@Autowired
	private ProductService productService;
	
	@RabbitListener(queues = "${app.config.rabbit.queue.product-stock}")
	public void recieveProductStockMessage(ProductStock productStock) {
		productService.updateProductStock(productStock);
	}
	
}
