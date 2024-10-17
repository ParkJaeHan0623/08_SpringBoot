package kr.parkjaehan.cookie_session.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {
    // 세션 저장을 위한 작성 페이지

    @GetMapping("/")
    public String home() {
        return "index";
    }

}
