package kr.parkjaehan.cookie_session.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.parkjaehan.cookie_session.helpers.UtilHelper;
import kr.parkjaehan.cookie_session.models.Member;

@Controller
public class SessionController {

    // 세션 저장을 위한 작성 페이지
    @GetMapping("/session/home")
    public String home(Model model, HttpServletRequest request) {
        // 1) request 객체를 사용해서 세션 객체 만들기
        HttpSession session = request.getSession();

        // 2) 세션 값 가져오기
        // 값으로 가져오는 것은 Object 타입이다 (형변환 필요. unboxing)
        // 기본 데이터 타입을 가져올 떄는 Wrapper 클래스를 사용한다.
        String userName = (String) session.getAttribute("user_name");
        Integer userAge = (Integer) session.getAttribute("user_age");

        // 3) view에 전달할 데이터 저장하기
        model.addAttribute("userName", userName);
        model.addAttribute("userAge", userAge);

        return "/session/home";
    }

    // 세션 저장 처리페이지

    @PostMapping("/session/save")
    public String save(HttpServletRequest request,
            @RequestParam("user_name") String userName,
            @RequestParam("user_age") int userAge) {
        // 1) request 객체를 사용해서 세션 객체 만들기
        HttpSession session = request.getSession();

        // 2) 세션 값 저장하기\
        // 값으로 저장하는것은 Object 타입이다 (형변환 필요. boxing)
        // 기본 데이터 타입을 저장할 때는 Wrapper 클래스를 사용한다.
        session.setAttribute("user_name", userName);
        session.setAttribute("user_age", userAge);

        return "redirect:/session/home";
    }

    // 심플 로그인 폼
    @GetMapping("/session/login")
    public String home(
            Model model,
            @CookieValue(value = "rememberId", required = false) String rememberCookie) {
        model.addAttribute("rememberId", rememberCookie);
        return "/session/login";
    }

    // 로그인 처리 페이지
    @SuppressWarnings("null")
    @PostMapping("/session/login_ok")
    public String loginOk(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("user_id") String userId,
            @RequestParam("user_pw") String userPw,
            @RequestParam(value = "remember_id", defaultValue = "N") String rememberId) {

        // 1) 로그인 가능 여부 검사
        if (!userId.equals("mitshowme") || !userPw.equals("1234")) {
            response.setStatus(403);
            response.setContentType("text/html; charset=utf-8");

            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.println("<script>");
            out.println("alert('아이디 또는 비밀번호가 일치하지 않습니다.');");
            out.println("history.back();");
            out.println("</script>");
            out.flush(); // 버퍼 비우기
            return null;
        }

        // 2) 로그인한 회원의 정보를 생성한다
        // 여기서는 POJO 클래스의 객체를 강제로 생성하지만, 실제로는 DB에서 가져와야 한다.
        // MyBatis를 사용하면 DB에서 가져온 회원정보를 객체로 만들어서 리턴받을 수 있다.

        Member member = new Member();
        member.setUserId(userId);
        member.setUserPw(userPw);

        HttpSession session = request.getSession();

        session.setAttribute("memberInfo", member);

        // 아이디 기억하기 처리
        if (rememberId.equals("Y")) {
            UtilHelper.getInstance().writeCookie(response, "rememberId", userId, 60 * 60 * 24 * 7);
        }

        return "redirect:/session/login";
    }

    /**
     * 로그아웃 처리 페이지
     * 로그인이 된 상태에서만 접근 가능해야 한다
     * -> 로그인이 된 상태 : 세션에 memberInfo가 저장되어 있을 때
     */
    @SuppressWarnings("null")
    @GetMapping("/session/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("memberInfo");

        if (member == null) {
            response.setStatus(403);
            response.setContentType("text/html; charset=utf-8");

            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }

            out.println("<script>");
            out.println("alert('로그인이 필요한 서비스입니다.');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return null;
        }
        session.invalidate();

        return "redirect:/session/login";
    }
}