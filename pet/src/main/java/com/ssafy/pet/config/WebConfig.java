package com.ssafy.pet.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.pet.interceptor.ConfirmInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final List<String> patterns = Arrays.asList(
			"/plan/**",
			"/user/updateimage",
			"/user/info/**",
			"/attraction/**",
			"/attraction/user-likes/**",
			"/review/**"
	);
	
	private final List<String> excludePatterns = Arrays.asList(
			"/plan/increase-plan-view-cnt"
	);
	
	private final ConfirmInterceptor confirmInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(confirmInterceptor).addPathPatterns(patterns)
		.excludePathPatterns(excludePatterns);
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")	//모든 요청에 대해
		.allowedOriginPatterns("*")	//모든 오리진에 적용
		.allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
		.allowedHeaders("*")
		.allowCredentials(true);
	}
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	System.out.println("현재 작업 디렉토리: " + System.getProperty("user.dir"));
        registry.addResourceHandler("/profile/**")
                .addResourceLocations("file:./src/main/resources/upload/profile/");
        registry.addResourceHandler("/reviews/**")
        		.addResourceLocations("file:./src/main/resources/upload/reviews/");
    }
}
