package kr.parkjaehan.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.helpers.Pagination;
import kr.parkjaehan.database.helpers.WebHelper;
import kr.parkjaehan.database.models.Department;
import kr.parkjaehan.database.services.DepartmentService;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DepartmentController {
    
    /** 학과 관리 서비스 객체 주입 */
    @Autowired
    private DepartmentService departmentService;

    /** WebHelper 주입 */
    @Autowired
    private WebHelper webHelper;

    /**
     * 학과 목록 화면
     * @param model 모델
     * @return - 학과 목록 화면을 구현한 View 경로
     */
    @GetMapping({"/", "/department"}) // 여러개의 주소를 받을 수 있음 - 확인 필요!!!!!!!!!!!
    public String index(Model model,
        // 검색어 파라미터 (페이지가 처음 열릴 때는 값 없음. 필수가 아님(required = false))
        @RequestParam(value = "keyword", required = false) String keyword,
        //페이지 구현에서 사용할 현재 페이지 번호
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
        Department input = new Department();
        input.setDname(keyword);    
        input.setLoc(keyword);
        
        List<Department> output = null;
        
        try {
            totalCount = departmentService.getCount(input);
            //페이지 번호 계산 --> 계산 결과를 로그로 출력
            pagination = new Pagination(nowPage, totalCount, listCount, pageCount);

            // SQL의 LIMIT 절에서 사용될 값을 Beans의 static 변수에 저장
            Department.setOffset(pagination.getOffset());
            Department.setListCount(pagination.getListCount());

            output = departmentService.getList(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        

        model.addAttribute("departments", output);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pagination", pagination);

        return "/department/index"; 
    } 

    /**
     * 학과 상세 화면
     * @param model - 모델
     * @param deptno - 학과번호
     * @return - 학과 상세 화면을 구현한 View 경로
     */
    @GetMapping("/department/detail/{deptno}")
    public String detail(Model model,
        @PathVariable("deptno") int deptno){ // 현재 여기에선 멤버 변수 그 자체와는 관계가 없음. 그냥 주고 받을때 쓰기 위한 값.
        
        // 조회 조건에 사용할 변수를 Beans에 저장
        Department input = new Department();
        input.setDeptno(deptno);

        // 조회 결과를 저장할 객체 선언
        Department item = null;

        try {
            item = departmentService.getItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }
        
        // View에 데이터 전달
        model.addAttribute("department", item);
        return "department/detail";
    }

    /**
     * 학과 등록 화면
     * @return - 학과 등록 화면을 구현한 View 경로
     */
    @GetMapping("/department/add")
    public String add(){
        return "department/add";
    }

    /**
     * 학과 등록 처리
     * Action 페이지들은 View를 사용하지않고 다른 페이지로 이동해야 하므로
     * 메서드 상단에 @ResponseBody를 적용하여 View 없이 직접 응답을 구현해야 한다.
     * 
     * @param dname 학과 이름
     * @param loc 학과 위치
     */
    @ResponseBody
    @PostMapping("/department/add_ok")
    public void addOk(HttpServletRequest request,
        @RequestParam("dname") String dname,
        @RequestParam("loc") String loc) {
        
        // 정상적인 경로로 접근한 경우 이전 페이지 주소는
        // 1) http://localhost:8080/department
        // 2) http://localhost:8080/department/detail/학과번호
        // 두 가지 경우가 있다.
        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/department")){
            webHelper.badRequest("올바르지 않은 접근입니다.");
            return;
        }

        // 저장할 값들을 Beans에 담는다.
        Department input = new Department();
        input.setDname(dname);
        input.setLoc(loc);

        try {
            departmentService.addItem(input); // 위에서 만든 input 객체는 참조로 전달 -> 여기서 추가되면서 pk값이 추가됨 -> 원본에도 pk 값이 추가됨.
        } catch (ServiceNoResultException e) { 
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // Insert, UPDATE, DELETE 처리를 수행하는 경우에는 리다이렉트로 이동
        // INSERT 결과를 확인할 수 있는 상세 페이지로 이동해야 한다.
        // 상세 페이지에 조회 대상의 PK 값을 전달해야 한다.
        webHelper.redirect("/department/detail/"+ input.getDeptno(), "등록되었습니다. "); // 위에서의 input 객체에 추가된 pk값을 가져오면 됨.
    }

    /**
     * 학과 삭제 처리
     * @param deptno 학과 번호
     */
    @ResponseBody
    @GetMapping("/department/delete/{deptno}")
    public void delete(HttpServletRequest request,
            @PathVariable("deptno") int deptno){
        
        // 이전 페이지 경로 검사 --> 정상적인 경로로 접근했는지 여부 확인
        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/department")){
            webHelper.badRequest("올바르지 않은 접근입니다.");
            return;
        }

        Department input = new Department();
        input.setDeptno(deptno);

        try {
            departmentService.deleteItem(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/department", "삭제되었습니다.");
    }
 
    /**
     * 학과 수정 페이지
     * @param model - 모델 객체
     * @param deptno - 학과 번호
     * @return - View 페이지의 경로
     */
    @GetMapping("/department/edit/{deptno}")
    public String edit(Model model,
        @PathVariable("deptno") int deptno){
            
            // 파라미터로 받은 PK값을 Beans 객체에 담는다
            // --> 검색 조건으로 사용하기 위함
            Department input = new Department();
            input.setDeptno(deptno);
            
            // 수정할 현재 데이터의 현재 값을 조회한다.
            Department item = null;

            try {
                item = departmentService.getItem(input);
            } catch (ServiceNoResultException e) {
                webHelper.serverError(e);
            } catch (Exception e) {
                webHelper.serverError(e);
            }

            // View에 데이터 전달
            model.addAttribute("department", item);
            
            return "department/edit";
        }

        /**
         * 학과 수정하는 액션 페이지
         */
        @ResponseBody
        @PostMapping("/department/edit_ok/{deptno}")
        public void edit_ok(
            @PathVariable("deptno") int deptno,
            @RequestParam("dname") String dname,
            @RequestParam("loc") String loc ){

            // 수정에 사용될 값을 Beans에 담는다.
            Department input = new Department();
            input.setDeptno(deptno);
            input.setDname(dname);
            input.setLoc(loc);

            // 데이터를 수정한다.

            try {
                departmentService.editItem(input);
            } catch (ServiceNoResultException e) {
                webHelper.serverError(e);
            } catch (Exception e) {
                webHelper.serverError(e);
            }

            // 수정 결과를 확인하기 위해서 상세 페이지로 이동
            webHelper.redirect("/department/detail/"+ input.getDeptno(), "수정되었습니다.");
        }
}
