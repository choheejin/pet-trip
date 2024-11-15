package com.ssafy.pet.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.pet.interceptor.ConfirmInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	
	private final List<String> patterns = Arrays.asList(
			"/plan/*",
			"/user/protected/*",
			"/attractions/hotplace/*"
	);
	
	private final ConfirmInterceptor confirmInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(confirmInterceptor).addPathPatterns(patterns);
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")	//모든 요청에 대해
		.allowedOriginPatterns("*")	//모든 오리진에 적용
		.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
		.allowCredentials(true);
	}
}
