package kr.parkjaehan.database.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 웹 헬퍼 클래스
 */
@Slf4j
@Component // 스프링에게 이 클래스가 빈(Bean)임을 알려줌
// 자동으로 할당하던 객체들을 싱글톤으로 관리하게 해줌
public class WebHelper {

    @Autowired
    private HttpServletRequest request; // 객체 주입을 했으므로 밑에서 파라미터로 받을 필요가 없다

    @Autowired
    private HttpServletResponse response; // 객체 주입을 했으므로 밑에서 파라미터로 받을 필요가 없다

    /**
     * 클라이언트 IP주소를 가져오는 메서드
     * 
     * @return 클라이언트 IP
     */
    public String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
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
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간(0 이면 지정안함, 음수일 경우 즉시 삭제)
     * @param domain - 쿠키 도메인
     * @param path - 쿠키 경로
     */
    public void writeCookie(String name, String value, int maxAge, String domain, String path) throws Exception {
        if (value != null && !value.equals("")) {
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("쿠키 값 인코딩 실패",e);
                throw e;
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
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간(0 이면 지정안함, 음수일 경우 즉시 삭제)
     * @param domain - 쿠키 도메인
     * 
     * @see #writeCookie(String, String, int, String, String)
     */
    public void writeCookie(String name, String value, int maxAge ,String domain) throws Exception {
        this.writeCookie(name, value, maxAge, domain , "/");
    }

    /**
     * 쿠키값을 저장한다. path값을 "/"로, domain을 null로 강제 설정한다
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간(0 이면 지정안함, 음수일 경우 즉시 삭제)
     */
    public void writeCookie(String name, String value, int maxAge) throws Exception {
        this.writeCookie(name, value, maxAge, null, "/");
    }

    /**
     * 쿠키값을 저장한다. path값을 "/"로, domain을 null로, maxAge를 0으로  강제 설정한다
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간(0 이면 지정안함, 음수일 경우 즉시 삭제)
     * 
     * @see #writeCookie(String, String, int, String, String)
     */
    public void writeCookie(String name, String value) throws Exception {
        this.writeCookie(name, value, 0, null, "/");
    }

    /**
     * 쿠키값을 삭제한다
     * @param name - 쿠키 이름
     */
    public void deleteCookie(String name) throws Exception {
        this.writeCookie(name, "", -1, null, "/");
    }

    /**
     * HTTP 상태 코드를 설정하고 메세지를 출력한 후, 지정된 페이지로 이동한다
     * 이동할 페이지가 없다면 이전 페이지로 이동한다
     * @param statusCode - HTTP 상태 코드 (예: 404)
     * @param url - 이동할 URL 
     * @param message - 출력할 메세지
     */
    public void redirect(int statusCode, String url, String message){
        response.setStatus(statusCode);
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = null;

        try {
            out = response.getWriter();
        } catch (IOException e) {
            log.error("응답 출력 스트림 생성 실패", e);
            return;
        }

        if (message != null && !message.equals("")) {
            out.println("<script>");
            out.println("alert('" + message + "');");
            out.println("</script>");
        }
        if (url != null && !url.equals("")) {
            out.println("<meta http-equiv='refresh' content='0; url=" + url + "' />");
        }
        else{
            out.println("<script>");
            out.println("history.back();");
            out.println("</script>");
        }
        out.flush();
    }

    /**
     * HTTP 상태 코드를 200으로 설정하고 메세지를 출력한 후, 지정된 페이지로 이동한다
     * @param url
     * @param message
     */
    public void redirect(String url, String message){
        this.redirect(200,url, message);
    }

    /**
     * HTTP 상태 코드를 200으로 설정하고 메세지 출력 없이 지정된 페이지로 이동한다
     * @param url
     * @param message
     */
    public void redirect(String url){
        this.redirect(200,url,null);
    }

    /**
     * HTTP 상태 코드를 설정하고 메세지 출력 없이 지정된 페이지로 이동한다
     * @param url
     * @param statusCode - HTTP 상태 코드 (예: 404)
     * @param message
     */
    public void redirect(int statusCode, String url){
        this.redirect(statusCode,url,null);
    }

    /**
     * 파라미터가 잘못된 경우에 호출할 이전 페이지 이동 기능
     * 
     * @param e - 에러 정보를 담고 있는 객체. Exception으로 선언했으므로 어떤 하위 객체가 전달되더라도 형변환 되어 받는다
     */
    public void badRequest(Exception e){
        this.redirect(403,null, e.getMessage());
    }

    /**
     * 파라미터가 잘못된 경우에 호출할 이전 페이지 이동 기능
     * @param message - 개발자가 직접 전달하는 에러 메세지
     */
    public void badRequest(String message){
        this.redirect(403,null, message);
    }

    /**
     * JAVA 혹은 SQL 쪽에서 잘못된 경우에 호출할 이전 페이지 이동 기능
     * @param e - 에러 정보를 담고 있는 객체. Exception으로 선언했으므로 어떤 하위 객체가 전달되더라도 형변환 되어 받는다
     */
    public void serverError(Exception e){
        String message = e.getMessage().trim().replace("'","\\'").split(System.lineSeparator())[0]; // 에러 메세지에서 줄바꿈 문자를 제거하고 첫 줄만 추출
        this.redirect(500,null, message);
    }

    /**
     * JAVA 혹은 SQL 쪽에서 잘못된 경우에 호출할 이전 페이지 이동 기능
     * @param message - 개발자가 직접 전달하는 에러 메세지
     */
    public void serverError(String message){
        this.redirect(500,null, message);
    }

}