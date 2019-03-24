package com.bhaveshshah.orderservice.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ServiceConfiguration implements WebMvcConfigurer     {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new ServiceInterceptor()).addPathPatterns("/**");
    }
}

