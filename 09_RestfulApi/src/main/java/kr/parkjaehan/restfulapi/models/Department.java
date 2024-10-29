package kr.parkjaehan.restfulapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Department 테이블의 구조를 정의하는 클래스
 */

@Data
@NoArgsConstructor  // 매개변수가 없는 생성자를 생성(for MyBatis)
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 생성(for MyBatis)
public class Department {
    private int deptno; // 테이블 필드 이름과 다를 수도 있음
    private String dname;
    private String loc;

    /**
     * 한 페이지에 표시될 목록 수
     * MYSQL의 Limit 절에서 사용할 값이므로 Beans에 추가한다
     * 
     * 1) static 변수로 선언하여 모든 객체가 공유한다
     * 2) static 변수는 객체 생성 없이 사용할 수 있다
     * 3) static 변수는 객체 생성 없이 클래스명으로 접근할 수 있다
     */
    @Getter
    @Setter
    private static int listCount = 0;

    /**
     * MYSQL의 Limit 절에서 사용할 오프셋 값
     * Beans에 추가한다
     * 
     * offset 위치부터 listCount 만큼의 데이터를 가져온다
     */
    @Getter
    @Setter
    private static int offset = 0;
}
