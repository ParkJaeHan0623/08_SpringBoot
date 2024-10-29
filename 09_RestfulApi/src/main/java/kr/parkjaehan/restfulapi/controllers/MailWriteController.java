package kr.parkjaehan.restfulapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메일 발송 폼을 출력하는 메소드
 * 웹 브라우저에게 HTML 파일을 전송한다.
 * 프론트엔드 역할을 하는 컨트롤러
 * 
 * @return 메일 발송 폼
 */
@Controller
public class MailWriteController {
    @GetMapping("/mail_form")
    public String mailForm() {
        return "mail_form.html";
    }
    
}
