package kr.parkjaehan.myshop.helpers;

import org.springframework.stereotype.Component;

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

    public String randomPassword(int maxLen){
        char[] rndAllCharacters = new char[]{
            //number
            '0','1','2','3','4','5','6','7','8','9',
            //uppercase
            'A','B','C','D','E','F','G','H','I','J','K','L','M',
            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            //lowercase
            'a','b','c','d','e','f','g','h','i','j','k','l','m',
            'n','o','p','q','r','s','t','u','v','w','x','y','z',
            //special symbols
            '!','@','#','$','%','^','&'
        };

        int charLen = rndAllCharacters.length;
        StringBuilder sb = new StringBuilder();
        char rndChar = '0';

        for(int i=0; i<maxLen; i++){
            int rnd = this.random(0, charLen-1);
            rndChar = rndAllCharacters[rnd];
            sb.append(rndChar);
        }
        return sb.toString();
    }
}