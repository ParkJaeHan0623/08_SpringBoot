package kr.parkjaehan.logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j // log 객체를 자동으로 생성(by lombok)
@Controller // 컨트롤러로 사용
public class HomeController {
    /**
     * `/` 라는 URL에 맵핑되는 메소드
     * URL이 `/` 로 지정될 경우 웹 사이트의 index 페이지로 사용됨
     * 
     * @param request HttpServletRequest 객체 (브라우저에서 요청한 정보)
     *                --> import jakarta.servlet.http.HttpServletRequest;
     * 
     * @return View 이름 (index.html)
     */

    @GetMapping("/")
    public String helloworld(Model model, HttpServletRequest request) {
        // 접근한 웹 브라우저의 IP 주소 얻기
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info(">>>> Proxy-Client-IP: " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info(">>>> WL-Proxy-Client-IP: " + ip);
        }

        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info(">>>> HTTP_CLIENT_IP: " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info(">>>> HTTP_X_FORWARDED_FOR: " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        model.addAttribute("clientIp", ip);
        log.debug(">>>> client IP: " + ip);

        // 접근한 웹 브라우저의 UserAgent 값 얻기
        String ua = request.getHeader("User-Agent");
        model.addAttribute("ua", ua);
        log.debug(">>>> User-Agent: " + ua);

        // UserAgent 값을 파싱하여 브라우저 정보 얻기
        // --> import ua_parser.Client;
        Parser uaParser = new Parser();
        // --> import ua_parser.Parser;
        Client c = uaParser.parse(ua);

        model.addAttribute("browserFamily", c.userAgent.family);
        model.addAttribute("browserMajor", c.userAgent.major);
        model.addAttribute("browserMinor", c.userAgent.minor);

        log.debug("browserFamily: " + c.userAgent.family);
        log.debug("browserMajor: " + c.userAgent.major);
        log.debug("browserMinor: " + c.userAgent.minor);

        model.addAttribute("osFamily", c.os.family);
        model.addAttribute("osMajor", c.os.major);
        model.addAttribute("osMinor", c.os.minor);

        log.debug("osFamily: " + c.os.family);
        log.debug("osMajor: " + c.os.major);
        log.debug("osMinor: " + c.os.minor);

        model.addAttribute("deviceFamily", c.device.family);

        log.debug("deviceFamily: " + c.device.family);

        String url = request.getRequestURL().toString();

        String methodName = request.getMethod();

        String queryString = request.getQueryString();

        if(queryString != null) {
            url = url + "?" + queryString;
        }

        model.addAttribute("method", methodName);
        model.addAttribute("url", url);

        return "index";

    }
}
