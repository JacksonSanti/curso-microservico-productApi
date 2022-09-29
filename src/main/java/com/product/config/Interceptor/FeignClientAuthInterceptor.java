package com.product.config.Interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.product.config.Exception.ValidationException;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignClientAuthInterceptor implements RequestInterceptor{
	
	private static final String AUTHORIZATION = "Authorization";
	
	
	@Override
	public void apply(RequestTemplate template) {
		var currentRequest = getCurrentRequest();
		
		template
			.header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION));
	}
	
	private HttpServletRequest getCurrentRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes())
					.getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("The current request couldn't be proccessed.");
		}
	}

}
