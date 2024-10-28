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
import kr.parkjaehan.database.helpers.WebHelper;
import kr.parkjaehan.database.models.Professor;
import kr.parkjaehan.database.services.ProfessorService;
import java.util.List;

@Controller
public class ProfessorController {
    
    /** 학과 관리 서비스 객체 주입 */
    @Autowired
    private ProfessorService professorService;

    /** WebHelper 주입 */
    @Autowired
    private WebHelper webHelper;

    /**
     * 학과 목록 화면
     * @param model 모델
     * @return - 학과 목록 화면을 구현한 View 경로
     */
    @GetMapping("/professor")
    public String index(Model model){ // response 일단 제외
        
        List<Professor> professors = null;

        try {
            professors = professorService.getList(null);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }

        model.addAttribute("professors", professors);
        return "/professor/index"; 
    } 

    @GetMapping("/professor/detail/{profno}")
    public String detail(Model model,
        @PathVariable("profno") int profno){ // 현재 여기에선 멤버 변수 그 자체와는 관계가 없음. 그냥 주고 받을때 쓰기 위한 값.
        
        // 조회 조건에 사용할 변수를 Beans에 저장
        Professor input = new Professor();
        input.setProfno(profno);

        // 조회 결과를 저장할 객체 선언
        Professor item = null;

        try {
            item = professorService.getItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }
        
        // View에 데이터 전달
        model.addAttribute("professor", item);
        return "professor/detail";
    }
    /// 이전에 한 내용
    /**
     * 교수 등록 화면
     * @return - 교수 등록 화면을 구현한 View 경로
     */
    @GetMapping("/professor/add")
    public String add(){
        return "professor/add";
    }

    /**
     * 교수 등록 처리
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
        @RequestParam(value = "comm", required= false) Integer comm, // 어떻게하면 null 값을 넣을 수 있을까? 테스트중. 1. 여기 세터에다가 직접 조건절
        @RequestParam("deptno") int deptno 
        ) {
        
        // 정상적인 경로로 접근한 경우 이전 페이지 주소는
        // 1) http://localhost:8080/department
        // 2) http://localhost:8080/department/detail/학과번호
        // 두 가지 경우가 있다.
        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/professor")){
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
    
        input.setComm(comm); // 이거 바꿔야 한다고 애들 알려줘야 함
        
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
        webHelper.redirect("/professor/detail/"+ input.getProfno(), "등록되었습니다. ");
    }

    /**
     * 학과 삭제 처리
     * @param profno 교수 번호
     */
    @ResponseBody
    @GetMapping("/professor/delete/{profno}")
    public void delete(HttpServletRequest request,
            @PathVariable("profno") int profno){
        
        // 이전 페이지 경로 검사 --> 정상적인 경로로 접근했는지 여부 확인
        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/professor")){
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
     * 학과 수정 페이지
     * @param model - 모델 객체
     * @param profno - 교수 번호
     * @return - View 페이지의 경로
     */
    @GetMapping("/professor/edit/{profno}")
    public String edit(Model model,
        @PathVariable("profno") int profno){
            
            // 파라미터로 받은 PK값을 Beans 객체에 담는다
            // --> 검색 조건으로 사용하기 위함
            Professor input = new Professor();
            input.setProfno(profno);
            
            // 수정할 현재 데이터의 현재 값을 조회한다.
            Professor item = null;

            try {
                item = professorService.getItem(input);
            } catch (ServiceNoResultException e) {
                webHelper.serverError(e);
            } catch (Exception e) {
                webHelper.serverError(e);
            }

            // View에 데이터 전달
            model.addAttribute("professor", item);
            
            return "professor/edit";
        }

        /**
         * 학과 수정하는 액션 페이지
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
            @RequestParam(value = "comm", required = false) Integer comm,
            @RequestParam("deptno") int deptno
            ){

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
            webHelper.redirect("/professor/detail/"+ input.getProfno(), "수정되었습니다.");
        }
    



}
