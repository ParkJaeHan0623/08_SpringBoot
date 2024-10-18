package kr.parkjaehan.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.parkjaehan.aop.interceptors.MyInterceptor;

@Configuration
@SuppressWarnings("null")
public class MyWebConfig implements WebMvcConfigurer{

    // MyInterceptor 객체를 자동으로 주입받는다
    // 이 과정에서 MyInterceptor안에 @Autowired로 선언된 UtilHelper 객체도 자동으로 주입된다
    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir = registry.addInterceptor(myInterceptor);
                ir.excludePathPatterns("/error","/robots.txt","/favicon.ico","/assets/**");
    }
}
