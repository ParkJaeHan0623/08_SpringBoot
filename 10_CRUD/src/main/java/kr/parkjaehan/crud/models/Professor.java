package kr.parkjaehan.crud.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Professor {
    private int profno;
    private String name;
    private String userid;
    private String position;
    private int sal;
    private String hiredate;
    private Integer comm; // NULL 허용이므로 Integer로 선언
    private int deptno;
    private String dname; //학과명 (조인을 통해 조회된 값)

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;

}
