package kr.parkjaehan.crud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DepartmentController {
    @GetMapping({"/department", "/department/list"})
    public String list() {
        return "department/list";
    }

    @GetMapping("/department/view/{deptno}")
    public String view(Model model, @PathVariable("deptno") int deptno) {
        model.addAttribute("deptno", deptno);
        return "department/view";
    }

    @GetMapping("/department/add")
    public String add() {
        return "department/add";
    }

    @GetMapping("/department/edit/{deptno}")
    public String edit(Model model, @PathVariable("deptno") int deptno) {
        model.addAttribute("deptno", deptno);
        return "department/edit";
    }
}
