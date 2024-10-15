package kr.parkjaehan.interceptor.helpers;

import jakarta.servlet.http.HttpServletRequest;

public class UtilHelper{
    private static UtilHelper current;

    public static UtilHelper getInstance() {
        if (current == null) {
            current = new UtilHelper();
        }

        return current;
    }

    private UtilHelper() {
        super();
    }

    /**
     * 랜덤 숫자 생성
     * @param min   최소값
     * @param max   최대값
     * @return      랜덤 숫자
     */
    public int random(int min, int max){
        int num = (int)((Math.random() * (max - min + 1))+ min);
        return num;
    }

    /**
     * 클라이언트 IP 가져오기
     * @param request   HttpServletRequest 객체
     * @return          클라이언트 IP
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
}