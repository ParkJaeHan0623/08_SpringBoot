package kr.parkjaehan.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.parkjaehan.interceptor.interceptors.MyInterceptor;

@Configuration
public class MyWebConfig implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir = registry.addInterceptor(new MyInterceptor());
                ir.excludePathPatterns("/hello","world");
    }
}
