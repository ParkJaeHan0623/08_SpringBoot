package kr.parkjaehan.database.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.models.Professor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ProfessorServiceTest {

    @Autowired
    private ProfessorService professorService; // 왜 임포트 해오는게 없지? -> 자동으로 객체주입. 인터페이스 선언해도 @Service 달린 구현체 가져옴
    
    @Test
    @DisplayName("과연 교수는 추가되는가?")
    void addServiceTest() throws Exception { // throws Exception 잠깐 빼뒀다가 마지막에 추가.  
        Professor input = new Professor();
        input.setName("고양이를연구하던미치광이");
        input.setUserid("mylifeforCAT");
        input.setPosition("고양이연구원");
        input.setSal(9999999);
        input.setHiredate("2020-02-20");
        input.setComm(10101010);
        input.setDeptno(102);

        Professor output = null;

        try {
            output = professorService.addItem(input);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
        } catch (Exception e) {
            log.error("Mapper 구현 에러", e);      
            throw e;  
        }

        if (output != null){
            log.debug("output: " + output.toString());
        }
    }

    @Test
    @DisplayName("과연 교수는 수정되는가?")
    void updateServiceTest() throws Exception { // throws Exception 나중에 추가
        Professor input = new Professor();
        input.setProfno(9915);
        input.setName("소련에서온수컷");
        input.setUserid("내이름한국고양이");
        input.setPosition("나눔신");
        input.setSal(9999999);
        input.setHiredate("2020-02-20");
        input.setComm(10101010);
        input.setDeptno(409);

        Professor output = null;

        try {
            output = professorService.editItem(input);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
        } catch (Exception e) {
            log.error("Mapper 구현 에러", e);      
            throw e;  
        }

        if (output != null){
            log.debug("output: " + output.toString());
        }
    }

    @Test
    @DisplayName("과연 교수는 삭제되는가?")
    void deleteServiceTest() throws Exception {// throws Exception 나중에 추가
        Professor input = new Professor();
        input.setProfno(9914);
    
        try {
            professorService.deleteItem(input);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
        } catch (Exception e) {
            log.error("Mapper 구현 에러", e);      
            throw e;  
        }
    }

    @Test
    @DisplayName("과연 하나의 교수는 조회되는가?")
    void selectItemServiceTest() throws Exception {// throws Exception 나중에 추가
        Professor input = new Professor();
        input.setProfno(9915);
    
        Professor output = null;
        try {
            output = professorService.getItem(input);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
        } catch (Exception e) {
            log.error("Mapper 구현 에러", e);      
            throw e;  
        }

        if (output != null){
            log.debug("output: " + output.toString());
        }
    }

    @Test
    @DisplayName("과연 수많은 교수는 조회되는가?")
    void selectListServiceTest() throws Exception {// throws Exception 나중에 추가
        List<Professor> output = null;

        Professor input = new Professor();
        // input.setDname("학과");

        try {
            output = professorService.getList(input);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
        } catch (Exception e) {
            log.error("Mapper 구현 에러", e);      
            throw e;  
        }

        if (output != null){
            for(Professor item : output){
                log.debug("output: " + item.toString());
            }
        }
    }
}
