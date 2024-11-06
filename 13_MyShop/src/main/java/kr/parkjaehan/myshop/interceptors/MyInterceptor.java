package kr.parkjaehan.myshop.interceptors;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import kr.parkjaehan.myshop.helpers.WebHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

@Slf4j
@Component
@SuppressWarnings("null")
public class MyInterceptor implements HandlerInterceptor {
    /** 페이지의 실행 시작 시간을 저장할 변수 */
    long startTime = 0;

    /** 페이지의 실행 완료시각을 저장할 변수 */
    long endTime = 0;

    /** WebHelper 객체를 자동 주입 받는다 */
    @Autowired
    private WebHelper webHelper;

    /**
     * Controller 실행 전에 수행되는 메서드
     * 클라이언트(웹브라우저)의 요청을 컨트롤러에 전달하기 전에 호출
     * 리턴값으로 boolean 값을 전달하는데, false인 경우,
     * controller를 실행시키지 않고 요청을 종료한다. 
     * 보통 이곳에서 각종 체크작업과 로그를 기록하는 작업을 진행한다.     
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        //log.debug("MyInterCeptors.preHandle 실행됨");

        log.info("---------- new client connected ----------");

        /** 1) 페이지의 실행 시작 시각을 구한다. */
        startTime = System.currentTimeMillis();

        /** 2) 접속한 클라이언트 정보 확인하기 */
        String ua = request.getHeader("user-agent");
        Parser uaParser = new Parser();
        Client c = uaParser.parse(ua);

        String fmt = "[Client] %s, %s, %s, %s, %s, %s";

        // String ipAddr = UtilHelper.getInstance().getClientIp(request); -> 이제 UtilHelper는 빈으로 등록되어 있으므로, new 키워드를 사용할 수 없다. // 거기에 이제 Utilhelper에서 WebHelper로 변경
        String ipAddr = webHelper.getClientIp(); // request 파라미터 삭제
        String osVersion = c.os.major + (c.os.minor != null ? "." + c.os.minor : "");
        String uaVersion = c.userAgent.major + (c.userAgent.minor != null ? "." + c.userAgent.minor : "");
        String clientInfo = String.format(fmt, ipAddr, c.userAgent.family, uaVersion, c.os.family, osVersion, c.device.family);

        log.info(clientInfo);

        /** 3) 클라이언트의 요청정보(URL) 확인하기 */
        // 현재 URL 획득
        String url = request.getRequestURL().toString();

        // GET 방식인지, POST 방식인지 조회한다.
        String methodName = request.getMethod();

        // URL에서 "?"이후에 전달되는 GET 파라미터 문자열을 모두 가져온다.
        String queryString = request.getQueryString();

        // 가져온 값이 있다면 URL과 결합하여 완전한 URL을 구성한다.
        if(queryString != null) {
            url = url + "?" + queryString;
        }

        // 획득한 정보를 로그로 표시한다.
        log.info(String.format("[%s], %s", methodName, url));

        /** 3) 클라이언트가 전달한 모든 파라미터 확인하기 */
        Map<String, String[]> params = request.getParameterMap(); // key-value 형태로 전달된 파라미터를 받음

        for (String key : params.keySet()) { // ex) a=100, b=200 -> 키셋은 [a, b]
            String[] value = params.get(key); // get(key)는 value값을 가져와서 100, 200 나옴.
            log.info(String.format("(params) <-- %s = %s", key, String.join(",", value))); // 전달된 파라미터와 그 값이 로그에 남음
        }                                                                                                   // 컨트롤러에서 처리를 않더라도, 로그에는 들어온 모든 파라미터가 남음.

        /** 4) 클라이언트가 머물렀던 이전 페이지 확인하기 */
        String referer = request.getHeader("referer");

        // 이전에 머물렀던 페이지가 존재한다면?
        // --> 직전 종료시간과 이번 접속 시작시각과의 차이는 이전 페이지에 머문 시간을 의미한다.
        if (referer != null && endTime >0){
            log.info(String.format("- Referer : time=%d, url = %s", startTime - endTime, referer));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);  
    }

    /**
     * view단으로 forward 되기 전에 수행
     * 컨트롤러 로직이 실행된 이후 호출된다
     * 컨트롤러 단에서 에러 발생시 해당 메서드는 수행되지 않는다.
     * request로 넘어온 데이터 가공시 많이 사용된다.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        @Nullable ModelAndView modelAndView) throws Exception {
        //log.debug("MyInterCeptors.postHandle 실행됨");

        /** 1) 페이지의 실행 종료 시각을 가져온다. */
        endTime = System.currentTimeMillis();

        /** 2) 컨트롤러가 실행하는데 걸린 시간을 구한다. */
        log.info(String.format("- duration : %d ms", endTime - startTime));

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 컨트롤러 종료 후 view가 정상적으로 렌더링 된 후 제일 마지막에 실행되는 메서드
     * (잘 사용하지 않음)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
        @Nullable Exception ex) throws Exception {
        //log.debug("MyInterCeptors.afterCompletion 실행됨");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
