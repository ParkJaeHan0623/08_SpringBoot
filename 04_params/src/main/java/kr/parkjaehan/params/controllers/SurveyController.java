package kr.parkjaehan.params.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SurveyController {
    @GetMapping("/survey")
    public String index() {
        return "survey/index";
    }

    @PostMapping("/survey/step1")
    public String step1(Model model,
            @RequestParam("name") String name) {
        model.addAttribute("name", name);
        return "survey/step1";
    }

    @PostMapping("/survey/step2")
    public String step2(Model model,
            @RequestParam("name") String name,
            @RequestParam("age") int age) {
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        return "survey/step2";
    }

    @GetMapping("/survey/step3")
    public String step3(Model model,
            @RequestParam("name") String name,
            @RequestParam("age") int age,
            @RequestParam("group") String group) {

        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("group", group);
        return "survey/step3";
    }

    @GetMapping("/survey/step4")
    public String step4(Model model,
            @RequestParam("name") String name,
            @RequestParam("age") int age,
            @RequestParam("group") String group,
            @RequestParam("glasses") String glasses) {

        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("group", group);
        model.addAttribute("glasses", glasses);
        return "survey/step4";
    }
}
