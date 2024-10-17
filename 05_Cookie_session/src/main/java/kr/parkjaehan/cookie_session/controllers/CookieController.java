package kr.parkjaehan.cookie_session.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.parkjaehan.cookie_session.helpers.UtilHelper;

@Controller
public class CookieController {
    // 쿠키 저장을 위한 작성 페이지
    @GetMapping("/cookie/home")
    public String home(Model model,
            // 쿠키 데이터 파싱
            @CookieValue(value = "name", defaultValue = "") String myCookieName,
            @CookieValue(value = "age", defaultValue = "0") int myCookieAge) {
        // 컨트롤러에서 쿠키를 식별하기 위한 처리
        try {
            // 저장시에 URLEncoding 처리한 쿠키값을 다시 디코딩
            myCookieName = URLDecoder.decode(myCookieName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 쿠키 값을 View에게 전달
        model.addAttribute("myCookieName", myCookieName);
        model.addAttribute("myCookieAge", myCookieAge);

        return "/cookie/home";
    }

    // 쿠키를 저장하기위한 action 페이지
    @PostMapping("/cookie/save")

    public String save(HttpServletResponse response,
            // value 값은 HTML의 name 속성값과 일치해야 한다
            // String과 int 타입의 변수는 HTML의 value 속성값을 받아온다
            @RequestParam(value = "cookie_name", defaultValue = "") String cookieName,
            @RequestParam(value = "cookie_time", defaultValue = "0") int cookieTime,
            @RequestParam(value = "cookie_var", defaultValue = "") String cookieVar) {

        // 1) 파라미터를 쿠키에 저장하기 위한 URLEncoding 처리

        if (!cookieVar.equals("")) {
            try {
                cookieVar = URLEncoder.encode(cookieVar, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // 2) 쿠키 저장
        // 저장할 쿠키 객체 생성
        Cookie cookie = new Cookie(cookieName, cookieVar);

        // 유효경로 --> 사이트 전역에 대한 설정
        cookie.setPath("/");

        // 유효 도메인(로컬 개발환경에서는 설정할 필요가 없음)
        // --> "www.naver.com" 인 경우 ".naver.com" 으로 설정
        // cookie.setDomain("localhost");

        // 유효시간 설정(0 이하라면 즉시 삭제, 초 단위)
        // 설정하지 않으면 브라우저가 종료될 때까지 유지
        cookie.setMaxAge(cookieTime);

        // 쿠키 저장
        response.addCookie(cookie);

        // 3) 강제 페이지 이동
        // 이 페이지에 머물렀다는 사실이 웹 브라우저의 history에 남지 않는다
        return "redirect:/cookie/home";
    }

    // 팝업창 제어 페이지
    @GetMapping("/cookie/popup")
    public String popup(Model model,
            // import org.springframework.web.bind.annotation.CookieValue;
            @CookieValue(value = "no-open", defaultValue = "") String noOpen) {
        // 쿠키값을 View에게 전달
        model.addAttribute("noOpen", noOpen);
        return "/cookie/popup";
    }

    @PostMapping("/cookie/popup_close")
    public String popupClose(HttpServletResponse response,
            @RequestParam(value = "no-open", defaultValue = "") String noOpen) {
        // 1)쿠키 저장하기
        // 60초 간 유효한 쿠키 설정
        // 실자 상용화 시에는 domain을 설정해야 한다
        UtilHelper.getInstance().writeCookie(response, "no-open", noOpen, 60);

        // 강제 페이지 이동
        return "redirect:/cookie/popup";
    }
}
