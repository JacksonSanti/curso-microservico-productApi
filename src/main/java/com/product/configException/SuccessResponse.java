package com.product.configException;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {

	private Integer status;
	private String message;
	
	public static SuccessResponse create(String message) {
		return SuccessResponse
				.builder()
				.status(HttpStatus.OK.value())
				.message(message)
				.build();
		
	}
	
}
