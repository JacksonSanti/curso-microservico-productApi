package com.product.model.sales.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.model.sales.dto.SalesConfirmationdto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SalesConfirmationSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
			
	@Value("${app.config.rabbit.exchange.product}")
	private String productTopicExchange;
	
	@Value("${app.config.rabbit.routingKey.sales-confirmation}")
	private String salesConfirmationKey;
	
	public void sendSalesConfirmationMessage(SalesConfirmationdto message) {
		try {
			log.info("Sending message: {}", new ObjectMapper().writeValueAsString(message));
			rabbitTemplate.convertAndSend(productTopicExchange,salesConfirmationKey,message);
			log.info("Message was sent successfully!");
		} catch (Exception e) {
			log.info("Error while trying to send sales confirmation message: ", e);
		}
		
		
	}
}
