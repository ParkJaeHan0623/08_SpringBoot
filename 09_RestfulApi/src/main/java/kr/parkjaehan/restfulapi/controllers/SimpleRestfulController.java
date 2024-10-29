package kr.parkjaehan.restfulapi.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import kr.parkjaehan.restfulapi.models.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
public class SimpleRestfulController {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 간단한 학과 정보를 JSON 형식으로 출력하는 메소드
     * 
     * @param response HTTP 응답 객체
     * @return JSON 형식으로 변환된 학과 정보
     */
    @GetMapping("/simple_department")
    public Map<String, Object> simpleDepartment(
            HttpServletResponse response) {
        // 1) JSON 형식 출력을 위한 HTTP 헤더설정
        // JSON 형식임을 명시함
        response.setContentType("application/json ; charset=UTF-8");
        // HTTP 상태 코드 설정(200: 정상, 404: 찾을 수 없음, 500: 서버 오류)
        response.setStatus(200);

        // 2) CORS 허용
        // 보안에 좋지 않기 때문에 이 옵션을 허용할 경우 인증키 등의 보안 장치가 필요함
        // 여기서는 실습을 위해 허용함
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 3) JSON 형식으로 변환될 Map 객체 구성
        Map<String, Object> department = new LinkedHashMap<String, Object>(); // 순서를 유지하는 Map 객체
        department.put("deptno", 101);
        department.put("dname", "컴퓨터공학과");
        department.put("loc", "1호관 101호");

        // 4) Spring 에 의해 JSON 형식으로 변환될 Map 객체 반환
        return department;
    }

    /**
     * 간단한 학과 정보 목록을 JSON 형식으로 출력하는 메소드
     * 
     * @param response HTTP 응답 객체
     * @return JSON 형식으로 변환된 학과 정보 목록
     */
    @GetMapping("/simple_department_list")
    public Map<String, Object> simpleDepartmentList(
            HttpServletResponse response) {
        // 1) JSON 형식 출력을 위한 HTTP 헤더설정
        // JSON 형식임을 명시함
        response.setContentType("application/json ; charset=UTF-8");
        // HTTP 상태 코드 설정(200: 정상, 404: 찾을 수 없음, 500: 서버 오류)
        response.setStatus(200);

        // 2) CORS 허용
        // 보안에 좋지 않기 때문에 이 옵션을 허용할 경우 인증키 등의 보안 장치가 필요함
        // 여기서는 실습을 위해 허용함
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 3) JSON 형식으로 변환될 Map 객체 구성
        Map<String, Object> departments = new LinkedHashMap<String, Object>(); // 순서를 유지하는 Map 객체

        List<Department> departmentList = new ArrayList<Department>();

        departmentList.add(new Department(101, "컴퓨터공학과", "1호관 101호"));
        departmentList.add(new Department(102, "멀티미디어학과", "2호관 202호"));
        departmentList.add(new Department(103, "전자공학과", "3호관 303호"));
        departmentList.add(new Department(104, "기계공학과", "4호관 404호"));

        departments.put("items", departmentList);

        return departments;
    }

    /**
     * 메일 발송을 위한 메소드(백엔드 기능을 담당한다)
     * 
     * @param response      HTTP 응답 객체
     * @param senderName    발신자 이름
     * @param senderEmail   발신자 이메일
     * @param receiverName  수신자 이름
     * @param receiverEmail 수신자 이메일
     * @param subject       제목
     * @param content       내용
     * @return JSON 으로 변환된 결과
     */
    @PostMapping("/sendmail")
    public Map<String, Object> sendMail(
            HttpServletResponse response,
            @RequestParam("sender-name") String senderName,
            @RequestParam("sender-email") String senderEmail,
            @RequestParam("receiver-name") String receiverName,
            @RequestParam("receiver-email") String receiverEmail,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content) {

        // 1) 리턴할 객체
        Map<String, Object> output = new LinkedHashMap<String, Object>();

        // 2) 메일 발송 정보 로그 확인
        log.debug("--------------------------------------------------------");
        log.debug(String.format("SenderName: %s", senderName));
        log.debug(String.format("SenderEmail: %s", senderEmail));
        log.debug(String.format("ReceiverName: %s", receiverName));
        log.debug(String.format("ReceiverEmail: %s", receiverEmail));
        log.debug(String.format("Subject: %s", subject));
        log.debug(String.format("Content: %s", content));
        log.debug("--------------------------------------------------------");

        // 3) Java Mail 라이브러리를 활용한 메일 발송
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        // 제목, 내용, 수신자 설정
        try {
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(new InternetAddress(receiverEmail, receiverName, "UTF-8"));
            helper.setFrom(new InternetAddress(senderEmail, senderName, "UTF-8"));

        } catch (MessagingException e) {
            response.setStatus(500);
            output.put("message", "메일 발송에 실패했습니다.");
            output.put("reason", e.getMessage());
            // 에러가 발생한 상황이므로 처리 중단을 위해 return
            return output;
        } catch (UnsupportedEncodingException e) {
            response.setStatus(500);
            output.put("message", "메일 발송에 실패했습니다.");
            output.put("reason", e.getMessage());
            // 에러가 발생한 상황이므로 처리 중단을 위해 return
            return output;
        }
        // 메일 발송
        javaMailSender.send(message);

        // 4) 결과 표시
        output.put("message", "메일이 발송되었습니다.");
        return output;
    }

    @GetMapping("/my_clac")
    public Map<String, Object> getCalc(
            HttpServletResponse response,
            @RequestParam("x") int x,
            @RequestParam("y") int y) {

        Map<String, Object> result = new LinkedHashMap<String, Object>(); // 순서를 유지하는 Map 객체

        result.put("x", x);
        result.put("y", y);
        result.put("result", x + y);

        return result;
    }

    @PostMapping("/my_clac")
    public Map<String, Object> postClac(
            HttpServletResponse response,
            @RequestParam("x") int x,
            @RequestParam("y") int y) {

        Map<String, Object> result = new LinkedHashMap<String, Object>(); // 순서를 유지하는 Map 객체

        result.put("x", x);
        result.put("y", y);
        result.put("result", x - y);

        return result;
    }

    @PutMapping("/my_clac")
    public Map<String, Object> putCalc(
            HttpServletResponse response,
            @RequestParam("x") int x,
            @RequestParam("y") int y) {

        Map<String, Object> result = new LinkedHashMap<>(); // 순서를 유지하는 Map 객체

        result.put("x", x);
        result.put("y", y);
        result.put("result", x * y); // 곱셈 수행

        return result;
    }

    @DeleteMapping("/my_clac")
    public Map<String, Object> deleteCalc(
            HttpServletResponse response,
            @RequestParam("x") int x,
            @RequestParam("y") int y) {

        Map<String, Object> result = new LinkedHashMap<>(); // 순서를 유지하는 Map 객체

        if (y == 0) {
            result.put("x", x);
            result.put("y", y);
            result.put("error", "나눌 수 없습니다");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 상태 코드 설정
        } else {
            result.put("x", x);
            result.put("y", y);
            result.put("result", x / y); // 나눗셈 수행
        }

        return result;
    }

}
