package kr.parkjaehan.params.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GetController {
    @GetMapping("/get/home")
    public String home() {
        return "get/home";
    }
    // GET 방식의 파라미터를 전송받기 위한 컨트롤러 메서드

    @GetMapping("/get/result")
    public String result(Model model,@RequestParam(value = "answer",defaultValue = "0") int myAnswer) {
        String result = null;

        if (myAnswer ==300) {
            result = "정답입니다.";
        }
        else {
            result = "오답입니다.";
        }

        model.addAttribute("result", result);
        model.addAttribute("myAnswer", myAnswer);
        
        return "get/result";
    }


}
