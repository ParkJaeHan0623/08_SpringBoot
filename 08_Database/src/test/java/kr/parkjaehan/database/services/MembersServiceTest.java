package kr.parkjaehan.database.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.models.Members;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@SpringBootTest
public class MembersServiceTest {
    @Autowired
    private MembersService membersService;

    @Test
    @DisplayName("멤버 추가 테스트")
    void insertMember() throws Exception {
        Members members = new Members();
        members.setUser_id("test");
        members.setUser_pw("test");
        members.setUser_name("테스트");
        members.setEmail("abcd@naver.com");
        members.setPhone("010-1234-5678");
        members.setBirthday("1990-01-01");
        members.setGender('M');
        members.setPostcode("12345");
        members.setAddr1("서울시 강남구");
        members.setAddr2("역삼동");
        members.setPhoto((byte) 1);
        members.setIs_out('N');
        members.setIs_admin('N');
        members.setLogin_date("2021-01-01");
        members.setReg_date("2021-01-01");
        members.setEdit_date("2021-01-01");

        Members result = membersService.addItem(members);

        try {
            result = membersService.getItem(result);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
        } catch (Exception e) {
            log.error("Mapper 구현 에러", e);
            throw e;
        }

        if (result != null) {
            log.info("result: " + result);
            log.error("new member id: " + members.getId());
        }
    }

    @Test
    @DisplayName("멤버 수정 테스트")
    void updateMember() throws Exception {
        Members members = new Members();
        members.setId(2);
        members.setUser_id("test");
        members.setUser_pw("test");
        members.setUser_name("박재한");
        members.setEmail("abcd@naver.com");
        members.setPhone("010-1234-5678");
        members.setBirthday("1990-01-01");
        members.setGender('M');
        members.setPostcode("12345");
        members.setAddr1("서울시 강남구");
        members.setAddr2("역삼동");
        members.setPhoto((byte) 1);
        members.setIs_out('N');
        members.setIs_admin('N');
        members.setLogin_date("2021-01-01");
        members.setReg_date("2021-01-01");
        members.setEdit_date("2021-01-01");

        Members result = null;

        try {
            result = membersService.editItem(members);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
            throw e;
        }

        if (result != null) {
            log.info("result: " + result.toString());
        }
    }
    @Test
    @DisplayName("멤버 삭제 테스트")
    void deleteMembers() throws Exception {
        Members members = new Members();
        members.setId(2);
        try {
            membersService.deleteItem(members);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
            throw e;
        }
    }

    @Test
    @DisplayName("단일행 멤버 조회 테스트")
    void selectOneMember() throws Exception {
        Members members = new Members();
        members.setId(4);
        Members result = null;

        try {
            result = membersService.getItem(members);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
        } catch (Exception e) {
            log.error("Mapper 구현 에러", e);
            throw e;
        }

        if (result != null) {
            log.info("result: " + result.toString());
        }
    }

    @Test
    @DisplayName("다중행 멤버 조회 테스트")
    void selectListMember() throws Exception {
        List<Members> output = null;

        Members input = new Members();
        input.setUser_name("박재한");

        try {
            output = membersService.getList(input);
        } catch (ServiceNoResultException e) {
            log.error("SQL문 처리 결과 없음", e);
        } catch (Exception e) {
            log.error("Mapper 구현 에러", e);
            throw e;
        }

        if (output != null) {
            for (Members item : output) {
                log.info("result: " + item.toString());
            }
        }
        
    }
    
}
