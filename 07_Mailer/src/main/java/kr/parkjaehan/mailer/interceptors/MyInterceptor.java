package kr.parkjaehan.mailer.interceptors;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.parkjaehan.mailer.helpers.UtilHelper;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

@Slf4j
@Component
@SuppressWarnings("null")
public class MyInterceptor implements HandlerInterceptor {
    // 페이지의 실행 시작 시각을 저장할 변수
    long startTime = 0;

    // 페이지의 실행 완료 시각을 저장할 변수
    long endTime = 0;

    // UtilHelper 객체를 자동으로 주입받는다
    @Autowired
    private UtilHelper utilHelper;

    /**
     * Controller 실행 전에 수행되는 메서드
     * 클라이언트(웹브라우저)의 요청을 컨트롤러에 전달 하기 전에 호출된다
     * return 값으로 boolean을 리턴하는데 false인 경우 Controller로 요청을 전달하지 않는다
     * 보통 이곳에서 각종 체크작업과 로그를 기록하는 작업을 진행한다
     */

     @Override
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         // log.debug("MyInterceptor.preHandle 실행");
         log.info("------------------new client connect ------------------");
         // 페이지의 실행 시작 시각을 저장
         startTime = System.currentTimeMillis();

         // 접속한 클라이언트 정보 확인
         String ua = request.getHeader("user-agent");
         Parser uaParser = new Parser();
         Client c = uaParser.parse(ua);

         String fmt = "[Client] %s %s %s %s %s";

        //  String ipAddr = UtilHelper.getInstance().getClientIp(request);
         String ipAddr = utilHelper.getClientIp(request); // 객체로 주입받아 사용 (클래스 X)
         String osVersion = c.os.major + (c.os.minor != null ? "." + c.os.minor : "");
         String uaVersion = c.userAgent.major + (c.userAgent.minor != null ? "." + c.userAgent.minor : "");
         String clientInfo = String.format(fmt, ipAddr, c.device.family,c.os.family,osVersion, c.userAgent.family, uaVersion);

         log.info(clientInfo);

         // 클라이언트의 요청정보(URL) 확인
        String url = request.getRequestURL().toString();

        // GET 방식인지, POST 방식인지 확인
        String methodName = request.getMethod();

        // URL에서 "?" 이후에 전달되는 GET파라미터 문자열을 가져옴
        String queryString = request.getQueryString();

        // 가져온 값이 있다면 URL과 결합하여 완전한 URL을 구성
        if (queryString != null) {
            url = url + "?" + queryString;
        }

        // 획득한 정보를 로그로 표시한다
        log.info(String.format("[%s] %s", methodName, url));

        // 3) 클라이언트가 전달한 모든 파라미터 확인
        Map<String, String[]> params = request.getParameterMap();

        for(String key : params.keySet()) {
            String[] value = params.get(key);
            
            log.info(String.format("[param] <-- %s = %s", key, String.join(",", value)));
            
        }

        // 4) 클라이언트가 머물렀던 이전 페이지 확인하기
        String referer = request.getHeader("referer");

        // 이전에 머물렀던 페이지가 존재한다면?
        // --> 직전 종료시간과 이번 접속의 시작시간과의 차이는 이전 페이지에 머문 시간을 의미한다
        if(referer != null && endTime > 0) {
            log.info(String.format("- REFERER : time=%d, url=%s", startTime - endTime, referer));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
     }

     /**
      * view 단으로 forward 되기 전에 수행되는 메서드
      * 컨트롤러 로직이 실행된 후 view를 호출하기 전에 수행된다
      * 컨트롤러 로직 실행 중 에러가 발생하면 이 메서드는 수행되지 않는다
      * request로 넘어온 데이터 가공시 많이 사용된다
      */
     @Override
     public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        //1) 컨트롤러의 실행 종료 시각을 가져온다
        endTime = System.currentTimeMillis();
        //2) 실행 시간 계산
        log.info(String.format("- 실행시간 : %d ms", endTime - startTime));
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
     }

     /**
      * 컨트롤러 종료 후 view 가 정삭적으로 렌더링 된 후 제일 마지막에 실행이 되는 메서드(잘 사용안함)
      */
     @Override
     public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
         
         HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
     }
    
}
