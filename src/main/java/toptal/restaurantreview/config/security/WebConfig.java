package dev.restaurantreview.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.restaurantreview.config.accesspolicy.AccessPolicyInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AccessPolicyInterceptor());
	}
}
