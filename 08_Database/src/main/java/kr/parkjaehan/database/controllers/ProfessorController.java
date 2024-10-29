package kr.parkjaehan.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.helpers.Pagination;
import kr.parkjaehan.database.helpers.WebHelper;
import kr.parkjaehan.database.models.Department;
import kr.parkjaehan.database.models.Professor;
import kr.parkjaehan.database.services.DepartmentService;
import kr.parkjaehan.database.services.ProfessorService;
import java.util.List;

@Controller
public class ProfessorController {

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
     * 교수 목록 화면
     * @param model
     * @param keyword
     * @param nowPage
     * @return
     */
    @GetMapping("/professor") // 여러개의 주소를 받을 수 있음 - 확인 필요!!!!!!!!!!!
    public String index(Model model,
            // 검색어 파라미터 (페이지가 처음 열릴 때는 값 없음. 필수가 아님(required = false))
            @RequestParam(value = "keyword", required = false) String keyword,
            // 페이지 구현에서 사용할 현재 페이지 번호
            @RequestParam(value = "page", defaultValue = "1") int nowPage) {
        /**
         * listCount, pageCount는 그때 그때 잡아주면 됨
         * static 변수는 객체로 접근
         */
        int totalCount = 0; // 전체 게시글 수
        int listCount = 10; // 한 페이지당 표시할 목록 수
        int pageCount = 5; // 한 그룹당 표시할 페이지 번호 수

        // 페이지 번호를 계산한 결과가 저장될 객체
        Pagination pagination = null;

        // 조회 조건에 사용할 객체
        Professor input = new Professor();
        input.setName(keyword);
        input.setUserid(keyword);

        List<Professor> output = null;

        try {
            totalCount = professorService.getCount(input);
            // 페이지 번호 계산 --> 계산 결과를 로그로 출력
            pagination = new Pagination(nowPage, totalCount, listCount, pageCount);

            // SQL의 LIMIT 절에서 사용될 값을 Beans의 static 변수에 저장
            Professor.setOffset(pagination.getOffset());
            Professor.setListCount(pagination.getListCount());

            output = professorService.getList(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("professors", output);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pagination", pagination);

        return "/professor/index";
    }


    /**
     * 교수 상세 화면
     * @param model
     * @param profno
     * @return
     */
    @GetMapping("/professor/detail/{profno}")
    public String detail(Model model,
            @PathVariable("profno") int profno) { // 현재 여기에선 멤버 변수 그 자체와는 관계가 없음. 그냥 주고 받을때 쓰기 위한 값.

        // 조회 조건에 사용할 변수를 Beans에 저장
        Professor input = new Professor();
        input.setProfno(profno);

        // 조회 결과를 저장할 객체 선언
        Professor output = null;

        try {
            output = professorService.getItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }

        // View에 데이터 전달
        model.addAttribute("professor", output);
        return "/professor/detail";
    }

    /**
     * 교수 등록 화면
     * 
     * @return - 교수 등록 화면을 구현한 View 경로
     */
    @GetMapping("/professor/add")
    public String add(Model model) {

        List<Department> output = null;
        
        try {
            output = departmentService.getList(null);
        } catch (Exception e) {
            webHelper.serverError(e);
        }
        model.addAttribute("departments", output);
        return "professor/add";
    }

    /**
     * 교수 등록 처리
     * 
     * @param name
     * @param userid
     * @param position
     * @param sal
     * @param hiredate
     * @param comm
     * @param deptno
     */
    @ResponseBody
    @PostMapping("/professor/add_ok")
    public void addOk(HttpServletRequest request,
            @RequestParam("name") String name, /////
            @RequestParam("userid") String userid,
            @RequestParam("position") String position,
            @RequestParam("sal") int sal,
            @RequestParam("hiredate") String hiredate,
            @RequestParam(value = "comm", required = false) Integer comm, 
            @RequestParam("deptno") int deptno) {

        // 정상적인 경로로 접근한 경우 이전 페이지 주소는
        // 1) http://localhost:8080/department
        // 2) http://localhost:8080/department/detail/학과번호
        // 두 가지 경우가 있다.
        String referer = request.getHeader("referer");

        if (referer == null || !referer.contains("/professor")) {
            webHelper.badRequest("올바르지 않은 접근입니다.");
            return;
        }

        // 저장할 값들을 Beans에 담는다.
        Professor input = new Professor();
        input.setName(name);
        input.setUserid(userid);
        input.setPosition(position);
        input.setSal(sal);
        input.setHiredate(hiredate);

        input.setComm(comm);

        input.setDeptno(deptno);

        try {
            professorService.addItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // Insert, UPDATE, DELETE 처리를 수행하는 경우에는 리다이렉트로 이동
        // INSERT 결과를 확인할 수 있는 상세 페이지로 이동해야 한다.
        // 상세 페이지에 조회 대상의 PK 값을 전달해야 한다.
        webHelper.redirect("/professor/detail/" + input.getProfno(), "등록되었습니다. ");
    }

    /**
     * 교수 삭제 처리
     * 
     * @param profno 교수 번호
     */
    @ResponseBody
    @GetMapping("/professor/delete/{profno}")
    public void delete(HttpServletRequest request,
            @PathVariable("profno") int profno) {

        // 이전 페이지 경로 검사 --> 정상적인 경로로 접근했는지 여부 확인
        String referer = request.getHeader("referer");

        if (referer == null || !referer.contains("/professor")) {
            webHelper.badRequest("올바르지 않은 접근입니다.");
            return;
        }

        Professor input = new Professor();
        input.setProfno(profno);

        try {
            professorService.deleteItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/professor", "삭제되었습니다.");
    }

    /**
     * 교수 수정 페이지
     * 
     * @param model  - 모델 객체
     * @param profno - 교수 번호
     * @return - View 페이지의 경로
     */
    @GetMapping("/professor/edit/{profno}")
    public String edit(Model model,
            @PathVariable("profno") int profno) {

        // 파라미터로 받은 PK값을 Beans 객체에 담는다
        // --> 검색 조건으로 사용하기 위함
        Professor input = new Professor();
        input.setProfno(profno);

        // 수정할 데이터와 모든 학과 목록을 조회한다
        Professor output = null;
        List<Department> output2 = null;

        try {
            output = professorService.getItem(input);
            output2 = departmentService.getList(null);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("professor", output);
        model.addAttribute("departments", output2);

        return "/professor/edit";
        
    }

    /**
     * 교수 수정하는 액션 페이지
     */
    @ResponseBody
    @PostMapping("/professor/edit_ok/{profno}")
    public void edit_ok(
            @PathVariable("profno") int profno,
            @RequestParam("name") String name,
            @RequestParam("userid") String userid,
            @RequestParam("position") String position,
            @RequestParam("sal") int sal,
            @RequestParam("hiredate") String hiredate,
            @RequestParam("comm") Integer comm,
            @RequestParam("deptno") int deptno) {

        // 수정에 사용될 값을 Beans에 담는다.
        Professor input = new Professor();
        input.setProfno(profno);
        input.setName(name);
        input.setUserid(userid);
        input.setPosition(position);
        input.setSal(sal);
        input.setHiredate(hiredate);
        input.setComm(comm);
        input.setDeptno(deptno);

        // 데이터를 수정한다.

        try {
            professorService.editItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // 수정 결과를 확인하기 위해서 상세 페이지로 이동
        webHelper.redirect("/professor/detail/" + input.getProfno(), "수정되었습니다.");
    }

}
