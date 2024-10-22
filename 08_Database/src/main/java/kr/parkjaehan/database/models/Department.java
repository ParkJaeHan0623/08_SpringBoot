package kr.parkjaehan.database.models;

import lombok.Data;

/**
 * Department 테이블의 구조를 정의하는 클래스
 */

@Data
public class Department {
    private int deptNo; // 테이블 필드이름과 다름에 주의
    private String dname;
    private String loc;
}
