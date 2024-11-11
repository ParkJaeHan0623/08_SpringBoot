package kr.parkjaehan.myshop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.parkjaehan.myshop.exceptions.ServiceNoResultException;
import kr.parkjaehan.myshop.mappers.MembersMapper;
import kr.parkjaehan.myshop.models.Members;
import kr.parkjaehan.myshop.services.MembersService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class MembersServiceImpl implements MembersService {
    @Autowired
    private MembersMapper membersMapper;

    @Override
    public Members addItem(Members input) throws ServiceNoResultException, Exception {
        int rows = membersMapper.insert(input);
        if (rows == 0) {
            throw new ServiceNoResultException("데이터 입력에 실패했습니다.");
        }
        return membersMapper.selectItem(input);
    }

    @Override
    public Members editItem(Members input) throws ServiceNoResultException, Exception {
        int rows = membersMapper.update(input);
        if (rows == 0) {
            throw new ServiceNoResultException("수정된 데이터가 없습니다.");
        }
        return membersMapper.selectItem(input);
    }

    @Override
    public int deleteItem(Members input) throws ServiceNoResultException, Exception {
        int rows = membersMapper.delete(input);
        if (rows == 0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다.");
        }
        return rows;
    }

    @Override
    public Members getItem(Members input) throws ServiceNoResultException, Exception {
        Members result = membersMapper.selectItem(input);
        if (result == null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다.");
        }
        return result;
    }

    @Override
    public List<Members> getList(Members input) throws ServiceNoResultException, Exception {
        return membersMapper.selectList(input);
    }

    @Override
    public void isUniqueUserId(String userId) throws Exception {
        Members input = new Members();
        input.setUser_id(userId);

        int output = 0;

        try {
            output = membersMapper.selectCount(input);

            if (output > 0) {
                throw new Exception("이미 사용중인 아이디입니다.");
            }
        } catch (Exception e) {
            log.error("아이디 중복 검사에 실패했습니다.",e);
            throw e;
        }
    }

    @Override
    public void isUniqueEmail(String email) throws Exception {
        Members input = new Members();
        input.setEmail(email);

        int output = 0;

        try {
            output = membersMapper.selectCount(input);

            if (output > 0) {
                throw new Exception("이미 사용중인 이메일입니다.");
            }
        } catch (Exception e) {
            log.error("이메일 중복 검사에 실패했습니다.",e);
            throw e;
        }
    }

    @Override
    public Members findId(Members input) throws Exception {
        Members output = null;

        try {
            output = membersMapper.findId(input);
            if (output == null) {
                throw new Exception("일치하는 회원정보가 없습니다.");
            }
        } catch (Exception e) {
            log.error("아이디 검색에 실패했습니다", e);
            throw e;
        }
        return output;

    }

    @Override
    public void resetPw(Members input) throws Exception {
       
        int rows = 0;
        
        try {
            rows = membersMapper.resetPw(input);
            if (rows == 0) {
                throw new Exception("아이디와 이메일을 확인하세요.");
            }
        } catch (Exception e) {
            log.error("Member 데이터 수정에 실패했습니다.", e);
            throw e;
        }
    }
    
}
