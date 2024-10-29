package kr.parkjaehan.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.helpers.Pagination;
import kr.parkjaehan.database.helpers.WebHelper;
import kr.parkjaehan.database.models.Department;
import kr.parkjaehan.database.models.Professor;
import kr.parkjaehan.database.models.Student;
import kr.parkjaehan.database.services.DepartmentService;
import kr.parkjaehan.database.services.ProfessorService;
import kr.parkjaehan.database.services.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
public class StudentController {

    /** 학과 관리 서비스 객체 주입 */
    @Autowired
    private StudentService studentService;

    /** 교수 관리 서비스 객체 주입 */
    @Autowired
    private ProfessorService professorService;

    /** 학과 관리 서비스 객체 주입 */
    @Autowired
    private DepartmentService departmentService;

    /** WebHelper 주입 */
    @Autowired
    private WebHelper webHelper;

    /**
     * 학과 목록 화면
     * 
     * @param model 모델
     * @return - 학과 목록 화면을 구현한 View 경로
     */
    @GetMapping("/student")
    public String index(Model model,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int nowPage) {
        int totalCount = 0; // 전체 게시글 수
        int listCount = 10; // 한 페이지당 표시할 목록 수
        int pageCount = 5; // 한 그룹당 표시할 페이지 번호 수

        // 페이지 번호를 계산한 결과가 저장될 객체
        Pagination pagination = null;
        Student input = new Student();
        input.setName(keyword);
        input.setUserid(keyword);

        List<Student> output = null;

        try {
            totalCount = studentService.getCount(input);
            pagination = new Pagination(nowPage, totalCount, listCount, pageCount);

            Student.setOffset(pagination.getOffset());
            Student.setListCount(pagination.getListCount());

            output = studentService.getList(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("students", output);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pagination", pagination);

        return "student/index";

    }

    @GetMapping("/student/detail/{studno}")
    public String detail(Model model,
            @PathVariable("studno") int studno) { // 현재 여기에선 멤버 변수 그 자체와는 관계가 없음. 그냥 주고 받을때 쓰기 위한 값.

        // 조회 조건에 사용할 변수를 Beans에 저장
        Student input = new Student();
        input.setStudno(studno);

        // 조회 결과를 저장할 객체 선언
        Student output = null;

        try {
            output = studentService.getItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }

        // View에 데이터 전달
        model.addAttribute("student", output);
        return "student/detail";
    }

    /**
     * 학생 등록 화면
     * 
     * @return - 학생 등록 화면을 구현한 View 경로
     */
    @GetMapping("/student/add")
    public String add(Model model) {
        List<Department> output = null;
        List<Professor> output2 = null;
        try {
            output = departmentService.getList(null);
            output2 = professorService.getList(null);
        } catch (Exception e) {
            webHelper.serverError(e);
        }
        model.addAttribute("departments", output);
        model.addAttribute("professors", output2);
        
        return "student/add";
    }

    /**
     * 학생 등록 처리
     * Action 페이지들은 View를 사용하지않고 다른 페이지로 이동해야 하므로
     * 메서드 상단에 @ResponseBody를 적용하여 View 없이 직접 응답을 구현해야 한다.
     * 
     */
    @ResponseBody
    @PostMapping("/student/add_ok")
    public void addOk(HttpServletRequest request,
            @RequestParam("name") String name,
            @RequestParam("userid") String userid,
            @RequestParam("grade") int grade,
            @RequestParam("idnum") String idnum,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("tel") String tel,
            @RequestParam("height") int height,
            @RequestParam("weight") int weight,
            @RequestParam("deptno") int deptno,
            @RequestParam("profno") Integer profno) {

        // 정상적인 경로로 접근한 경우 이전 페이지 주소는
        // 1) http://localhost:8080/student
        // 2) http://localhost:8080/student/detail/학과번호
        // 두 가지 경우가 있다.
        String referer = request.getHeader("referer");

        if (referer == null || !referer.contains("/student")) {
            webHelper.badRequest("올바르지 않은 접근입니다.");
            return;
        }

        // 저장할 값들을 Beans에 담는다.
        Student input = new Student();
        input.setName(name);
        input.setUserid(userid);
        input.setGrade(grade);
        input.setIdnum(idnum);
        input.setBirthdate(birthdate);
        input.setTel(tel);
        input.setHeight(height);
        input.setWeight(weight);
        input.setDeptno(deptno);
        input.setProfno(profno);

        try {
            studentService.addItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // Insert, UPDATE, DELETE 처리를 수행하는 경우에는 리다이렉트로 이동
        // INSERT 결과를 확인할 수 있는 상세 페이지로 이동해야 한다.
        // 상세 페이지에 조회 대상의 PK 값을 전달해야 한다.
        webHelper.redirect("/student/detail/" + input.getStudno(), "등록되었습니다. ");
    }

    /**
     * 학생 삭제 처리
     * 
     * @param studno 학생 번호
     */
    @ResponseBody
    @GetMapping("/student/delete/{studno}")
    public void delete(HttpServletRequest request,
            @PathVariable("studno") int studno) {

        // 이전 페이지 경로 검사 --> 정상적인 경로로 접근했는지 여부 확인
        String referer = request.getHeader("referer");

        if (referer == null || !referer.contains("/student")) {
            webHelper.badRequest("올바르지 않은 접근입니다.");
            return;
        }

        Student input = new Student();
        input.setStudno(studno);

        try {
            studentService.deleteItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/student", "삭제되었습니다.");
    }

    /**
     * 학생 수정 페이지
     * 
     * @param model  - 모델 객체
     * @param studno - 학생 번호
     * @return - View 페이지의 경로
     */
    @GetMapping("/student/edit/{studno}")
    public String edit(Model model,
            @PathVariable("studno") int studno) {

        // 파라미터로 받은 PK값을 Beans 객체에 담는다
        // --> 검색 조건으로 사용하기 위함
        Student input = new Student();
        input.setStudno(studno);

        // 수정할 현재 데이터의 현재 값을 조회한다.
        Student output = null;
        List<Department> output2 = null;
        List<Professor> output3 = null;

        try {
            output = studentService.getItem(input);
            output2 = departmentService.getList(null);
            output3 = professorService.getList(null);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("student", output);
        model.addAttribute("departments", output2);
        model.addAttribute("professors", output3);

        return "student/edit";
    }

    /**
     * 학과 수정하는 액션 페이지
     */
    @ResponseBody
    @PostMapping("/student/edit_ok/{studno}")
    public void edit_ok(
            @PathVariable("studno") int studno,
            @RequestParam("name") String name,
            @RequestParam("userid") String userid,
            @RequestParam("grade") int grade,
            @RequestParam("idnum") String idnum,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("tel") String tel,
            @RequestParam("height") int height,
            @RequestParam("weight") int weight,
            @RequestParam("deptno") int deptno,
            @RequestParam("profno") Integer profno) {

        // 수정에 사용될 값을 Beans에 담는다.
        Student input = new Student();
        input.setStudno(studno);
        input.setName(name);
        input.setUserid(userid);
        input.setGrade(grade);
        input.setIdnum(idnum);
        input.setBirthdate(birthdate);
        input.setTel(tel);
        input.setHeight(height);
        input.setWeight(weight);
        input.setDeptno(deptno);
        input.setProfno(profno);

        // 데이터를 수정한다.

        try {
            studentService.editItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // 수정 결과를 확인하기 위해서 상세 페이지로 이동
        webHelper.redirect("/student/detail/" + input.getStudno(), "수정되었습니다.");
    }
}
