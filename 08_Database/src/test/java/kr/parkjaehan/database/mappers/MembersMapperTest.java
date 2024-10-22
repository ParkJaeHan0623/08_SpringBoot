package kr.parkjaehan.database.mappers;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.parkjaehan.database.models.Members;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MembersMapperTest {
    @Autowired
    private MembersMapper membersMapper;

    @Test
    @DisplayName("회원 추가 테스트")
    void insertMembers() {
        Members input = new Members();
        input.setUser_id("test");
        input.setUser_pw("test");
        input.setUser_name("테스트");
        input.setEmail("abcd@naver.com");
        input.setPhone("010-1234-5678");
        input.setBirthday("2021-06-01");
        input.setGender('M');
        input.setPostcode("12345");
        input.setAddr1("서울시 강남구");
        input.setAddr2("테스트동");
        input.setPhoto(null);
        input.setIs_out('N');
        input.setIs_admin('N');
        input.setLogin_date("2021-06-01");
        input.setReg_date("2021-06-01");
        input.setEdit_date("2021-06-01");

        int output = membersMapper.insert(input);

        log.debug("output = " + output);
        log.debug("new id = " + input.getId());
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void updateMembers() {
        Members input = new Members();
        input.setId(1);
        input.setUser_id("test");
        input.setUser_pw("test");
        input.setUser_name("테스트");
        input.setEmail("abcd@naver.com");
        input.setPhone("010-1234-5678");
        input.setBirthday("2021-06-01");
        input.setGender('M');
        input.setPostcode("12345");
        input.setAddr1("서울시 강남구");
        input.setAddr2("테스트동");
        input.setPhoto(null);
        input.setIs_out('N');
        input.setIs_admin('N');
        input.setLogin_date("2021-06-01");
        input.setReg_date("2021-06-01");
        input.setEdit_date("2021-06-01");

        int output = membersMapper.update(input);

        log.debug("output = " + output);
        log.debug("new id = " + input.getId());
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteMembers() {
        Members input = new Members();
        input.setId(1);

        int output = membersMapper.delete(input);

        log.debug("output = " + output);
    }

    
    @Test
    @DisplayName("회원 단일 조회 테스트")
    void selectMembers() {
        Members input = new Members();
        input.setId(2);

        Members output = membersMapper.selectItem(input);

        log.debug("output = " + output.toString());
    }

    @Test
    @DisplayName("회원 목록 조회 테스트")
    void selectListMembers(){
        List<Members> output = membersMapper.selectList(null);

        for(Members item : output){
            log.debug("item = " + item.toString());
        }
    }
}