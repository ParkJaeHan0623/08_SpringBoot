package kr.parkjaehan.myshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.parkjaehan.myshop.interceptors.MyInterceptor;

@Configuration
@SuppressWarnings("null")
public class MyWebConfig implements WebMvcConfigurer{

    // MyInterceptor 객체를 자동 주입 받는다
    // 이 과정에서 myInterceptor 안에 @Autowired가 붙어있는 MyInterceptor 객체도 자동 주입된다.
    @Autowired
    private MyInterceptor myInterceptor;

    // 업로드 된 파일이 저장될 경로 (application.properties로부터 읽어옴)
    // --> import org.springframework.beans.factory.annotation.Value;
    @Value("${upload.dir}")
    private String uploadDir;

    // 업로드 된 파일이 노출될 URL 경로 (application.properties로부터 읽어옴)
    @Value("${upload.url}")
    private String uploadUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 직접 정의한 MyInterceptor를 Spring에 등록. new MyInterceptor()를 myInterceptor로 대체.
        InterceptorRegistration ir = registry.addInterceptor(myInterceptor); // @autowired가 붙은 클래스 또는 그런 메소드를 소유하고 있는 대상은  
        // 해당 경로는 인터셉터가 가로채지 않는다.                                 // new 키워드를 사용할 수 없게 된다.
        ir.excludePathPatterns("/error", "/robots.txt", "/favicon.ico", "/assets/**");
        // 위에 /hello 가 있기 때문에, MyInterceptor의 작동을 막는 /hello 경로에서의 활동이 로그에 저장되지 않는다. // 내가 추가한 주석
    }

    // 설정 파일에 명시된 업로드 저장 경로와 URL 상의 경로를 맵핑시킴
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(String.format("%s/**", uploadUrl))
                .addResourceLocations(String.format("file://%s/", uploadDir));
    }
}
