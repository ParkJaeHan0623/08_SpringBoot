package kr.parkjaehan.database.mappers;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.parkjaehan.database.models.Department;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class DepartmentMapperTest {
    // 테스트 클래스에서는 객체 주입을 사용해야 함
    @Autowired
    private DepartmentMapper departmentMapper;

    @Test
    @DisplayName("학과 추가 테스트")
    void insertDepartment() {
        Department input = new Department();
        input.setDname("테스트학과");
        input.setLoc("테스트강의실");

        int output = departmentMapper.insert(input);

        // 저장된 데이터의 수
        log.debug("output = " + output);

        // 생성된 PK 값
        log.debug("new deptno = " + input.getDeptNo());
    }

    @Test
    @DisplayName("학과 수정 테스트")
    void updateDepartment() {
        Department input = new Department();
        input.setDeptNo(102);
        input.setDname("테스트학과");
        input.setLoc("테스트강의실");

        int output = departmentMapper.update(input);

        log.debug("output = " + output);
    }

    @Test
    @DisplayName("학과 삭제 테스트")
    void deleteDepartment() {
        Department input = new Department();
        input.setDeptNo(203);

        int output = departmentMapper.delete(input);

        log.debug("output = " + output);
    }

    @Test
    @DisplayName("학과 단일 조회 테스트")
    void selectDepartment() {
        Department input = new Department();
        input.setDeptNo(102);

        Department output = departmentMapper.selectItem(input);

        log.debug("output = " + output.toString());
    }

    @Test
    @DisplayName("학과 목록 조회 테스트")
    void selectListDepartment(){
        List<Department> output = departmentMapper.selectList(null);

        for(Department item : output){
            log.debug("item = " + item.toString());
        }
    }

}
