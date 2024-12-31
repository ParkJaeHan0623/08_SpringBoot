package kr.parkjaehan.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

// Scheduler를 사용하기 위한 어노테이션
@EnableScheduling
@SpringBootApplication
public class MyshopApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MyshopApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(MyshopApplication.class, args);
	}

}
