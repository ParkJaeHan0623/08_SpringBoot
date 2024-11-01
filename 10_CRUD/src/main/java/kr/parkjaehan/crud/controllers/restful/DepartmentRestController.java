package kr.parkjaehan.crud.controllers.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import kr.parkjaehan.crud.exceptions.ServiceNoResultException;
import kr.parkjaehan.crud.helpers.Pagination;
import kr.parkjaehan.crud.helpers.RestHelper;
import kr.parkjaehan.crud.models.Department;
import kr.parkjaehan.crud.services.DepartmentService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class DepartmentRestController {
    
    /** 학과 관리 서비스 객체 주입 */
    @Autowired
    private DepartmentService departmentService;

    /** RestHelper 주입 */
    @Autowired
    private RestHelper restHelper;

    /**
     * 학과 목록 API
     * @param keyword
     * @param nowPage
     * @return 학과 목록을 포함하는 JSON 데이터
     */
    @GetMapping("/api/department") // 여러개의 주소를 받을 수 있음
    public Map<String,Object> getList(
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
           return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("keyword", keyword);
        data.put("item", output);   // 본체를 담고있는 데이터는 item이라는 이름으로 담는다
        data.put("pagination", pagination);

        return restHelper.sendJson(data);
    } 

    /**
     * 학과 상세 API
     * @param deptno 학과 번호
     * @return 학과 상세 정보를 포함하는 JSON 데이터
     */
    @GetMapping("/api/department/{deptno}")
    public Map<String,Object> detail(
        @PathVariable("deptno") int deptno){ // 현재 여기에선 멤버 변수 그 자체와는 관계가 없음. 그냥 주고 받을때 쓰기 위한 값.
        
        // 조회 조건에 사용할 변수를 Beans에 저장
        Department input = new Department();
        input.setDeptno(deptno);

        // 조회 결과를 저장할 객체 선언
        Department item = null;

        try {
            item = departmentService.getItem(input);
        } catch (ServiceNoResultException e) {
            return restHelper.serverError(e);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }
        
        // View에 데이터 전달
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", item); // 본체를 담고있는 데이터는 item이라는 이름으로 담는다
        return restHelper.sendJson(data);
    }

    /**
     * 학과 등록 처리
     * Action 페이지들은 View를 사용하지않고 다른 페이지로 이동해야 하므로
     * 메서드 상단에 @ResponseBody를 적용하여 View 없이 직접 응답을 구현해야 한다.
     * 
     * @param dname 학과 이름
     * @param loc 학과 위치
     */

    @PostMapping("/api/department")
    public Map<String, Object> addOk(HttpServletRequest request,
        @RequestParam("dname") String dname,
        @RequestParam("loc") String loc) {
        
        // 저장할 값들을 Beans에 담는다.
        Department input = new Department();
        input.setDname(dname);
        input.setLoc(loc);

        Department output = null; // 기존에는 리턴을 받지 않았지만 여기서는 리턴을 받아서 JSON으로 넣어줘야 함.

        try {
            output = departmentService.addItem(input); // 리턴을 받아서 JSON으로 넣어줘야 함.
        } catch (ServiceNoResultException e) { 
            return restHelper.serverError(e);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }

    /**
     * 학과 삭제 처리
     * @param deptno 학과 번호
     */
    @DeleteMapping("/api/department/{deptno}")
    public Map<String, Object> delete(HttpServletRequest request,
            @PathVariable("deptno") int deptno){

        Department input = new Department();
        input.setDeptno(deptno);

        try {
            departmentService.deleteItem(input);
        } catch (ServiceNoResultException e) {
            return restHelper.serverError(e);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        return restHelper.sendJson();
    }
 
        /**
         * 학과 수정하는 액션 페이지
         */

        @PutMapping("/api/department/{deptno}")
        public Map<String, Object> edit_ok(
            @PathVariable("deptno") int deptno,
            @RequestParam("dname") String dname,
            @RequestParam("loc") String loc ){

            // 수정에 사용될 값을 Beans에 담는다.
            Department input = new Department();
            input.setDeptno(deptno);
            input.setDname(dname);
            input.setLoc(loc);

            Department output = null;

            // 데이터를 수정한다.

            try {
                output = departmentService.editItem(input);
            } catch (ServiceNoResultException e) {
                return restHelper.serverError(e);
            } catch (Exception e) {
                return restHelper.serverError(e);
            }

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("item", output);

            return restHelper.sendJson(data);
        }
}
