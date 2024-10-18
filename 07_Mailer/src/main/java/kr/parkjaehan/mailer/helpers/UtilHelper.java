package kr.parkjaehan.mailer.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 유틸리티 헬퍼 클래스
 */
@Component // 스프링에게 이 클래스가 빈(Bean)임을 알려줌
// 자동으로 할당하던 객체들을 싱글톤으로 관리하게 해줌
public class UtilHelper {

    /**
     * 랜덤 숫자 생성
     * 
     * @param min 최소값
     * @param max 최대값
     * @return 랜덤 숫자
     */
    public int random(int min, int max) {
        int num = (int) ((Math.random() * (max - min + 1)) + min);
        return num;
    }

    /**
     * 클라이언트 IP 가져오기
     * 
     * @param request HttpServletRequest 객체
     * @return 클라이언트 IP
     */
    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 쿠키값을 저장한다
     * @param response - HttpServletResponse 객체
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간(0 이면 지정안함, 음수일 경우 즉시 삭제)
     * @param domain - 쿠키 도메인
     * @param path - 쿠키 경로
     */
    public void writeCookie(HttpServletResponse response, String name, String value, int maxAge, String domain, String path) {
        if (value != null && !value.equals("")) {
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);

        if (domain != null) {
            cookie.setDomain(domain);
        }

        if(maxAge != 0){
            cookie.setMaxAge(maxAge);
        }

        response.addCookie(cookie);

    }

    /**
     * 쿠키값을 저장한다. path값을 "/"로 강제 설정한다
     * @param response - HttpServletResponse 객체
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간(0 이면 지정안함, 음수일 경우 즉시 삭제)
     * @param domain - 쿠키 도메인
     * 
     * @see #writeCookie(HttpServletResponse, String, String, int, String, String)
     */
    public void writeCookie(HttpServletResponse response, String name, String value, int maxAge ,String domain) {
        this.writeCookie(response, name, value, maxAge, domain , "/");
    }

    /**
     * 쿠키값을 저장한다. path값을 "/"로, domain을 null로 강제 설정한다
     * @param response  - HttpServletResponse 객체
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간(0 이면 지정안함, 음수일 경우 즉시 삭제)
     */
    public void writeCookie(HttpServletResponse response, String name, String value, int maxAge) {
        this.writeCookie(response, name, value, maxAge, null, "/");
    }

    /**
     * 쿠키값을 저장한다. path값을 "/"로, domain을 null로, maxAge를 0으로  강제 설정한다
     * @param response - HttpServletResponse 객체
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간(0 이면 지정안함, 음수일 경우 즉시 삭제)
     * 
     * @see #writeCookie(HttpServletResponse, String, String, int, String, String)
     */
    public void writeCookie(HttpServletResponse response, String name, String value) {
        this.writeCookie(response, name, value, 0, null, "/");
    }

    /**
     * 쿠키값을 삭제한다
     * @param response  - HttpServletResponse 객체
     * @param name - 쿠키 이름
     */
    public void deleteCookie(HttpServletResponse response, String name){
        this.writeCookie(response, name, "", -1, null, "/");
    }


    /**
     * 예외 메시지를 출력하고, 이전 페이지로 이동한다
     * @param response
     * @param statusCode
     * @param url
     * @param message
     */
    public void redirect(HttpServletResponse response, int statusCode, String url, String message){
        response.setStatus(statusCode);
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = null;

        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (message != null && !message.equals("")) {
            out.println("<meta http-equiv='refresh' content='0; url=" + url + "'>");
        }
        if (url != null && !url.equals("")) {
            out.println("<script>");
            out.println("location.href='" + url + "';");
            out.println("</script>");
        } else{
            out.println("<script>");
            out.println("history.back();");
            out.println("</script>");
        }

        out.flush();

    }
    
}