package kr.parkjaehan.hellospring;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

@SpringBootApplication
// 웹페이지로서 동작 가능한 기능을 탑재
@Controller
public class HellospringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HellospringApplication.class, args);
	}

	/*1) 서블릿 형태의 컨트롤러 */
	// 리턴하는 문자열을 웹 브라우저에게 전달한다
	// 이 항목을 명시하지 않을 경우 리턴하는 문자열은 view의 파일명이 됨.
	// --> import org.springframework.web.bind.annotation.ResponseBody;

	@ResponseBody
	// 이 메서드가 연결될 URL을 지정한다.
	@GetMapping("/hellospring")
	public String hello(){
		String message = "<h1>Hello SpringBoot</h1>";
		message += "<h2>안녕하세요 스프링</h2>";
		System.out.println(message);
		return message;
	}

	/*2) 자동으로 View의 이름을 찾는 컨트롤러 */
	// import org.springframework.ui.Model;

	@GetMapping("/now")
	public void world(Model model){
		// import java.util.Date;
		model.addAttribute("nowtime", new Date().toString());

		// void형의 메서드이므로 이 메서드가 사용하는 view의 이름은 URL과 같다.(now.html)
	}

	/*3) View의 이름을 반환하는 컨트롤러 */

	@GetMapping("/today")
	public String today(Model model){
		model.addAttribute("message1","스프링부트 View 테스트 입니다");
		model.addAttribute("message2","안녕하세요");
		model.addAttribute("message3","반갑습니다");

		// String형의 메서드이므로 이 메서드가 사용하는 view의 이름은 리턴값이 된다.
		return "myview";
	}

}
