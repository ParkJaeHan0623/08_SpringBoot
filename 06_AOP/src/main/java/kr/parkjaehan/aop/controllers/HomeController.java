package kr.parkjaehan.aop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import kr.parkjaehan.aop.services.MyCalcService;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    // MyCalcService 객체를 주입받음
    @Autowired
    private MyCalcService myCalcService;

    @GetMapping("/")
    public String home(Model model) {
        int value1 = myCalcService.plus(10, 5);
        int value2 = myCalcService.minus(10, 5);

        model.addAttribute("value1", value1);
        model.addAttribute("value2", value2);

        return "index";
    }
    
}
