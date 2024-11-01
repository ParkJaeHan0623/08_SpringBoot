package kr.parkjaehan.crud.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.parkjaehan.crud.exceptions.ServiceNoResultException;
import kr.parkjaehan.crud.mappers.ProfessorMapper;
import kr.parkjaehan.crud.mappers.StudentMapper;
import kr.parkjaehan.crud.models.Professor;
import kr.parkjaehan.crud.models.Student;
import kr.parkjaehan.crud.services.ProfessorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfessorServiceImpl implements ProfessorService{

    @Autowired
    private ProfessorMapper professorMapper;

    @Autowired
    private StudentMapper studentMapper;
    
    @Override
    public Professor addItem(Professor input) throws ServiceNoResultException, Exception {
        int rows = professorMapper.insert(input);

        if(rows == 0){
            throw new ServiceNoResultException("저장된 데이터가 없습니다");
        }

        return professorMapper.selectItem(input);
    }

    @Override
    public Professor editItem(Professor input) throws ServiceNoResultException, Exception {
        int rows = professorMapper.update(input);

        if(rows == 0){
            throw new ServiceNoResultException("수정된 데이터가 없습니다");
        }

        return professorMapper.selectItem(input);
    }

    @Override
    public int deleteItem(Professor input) throws ServiceNoResultException, Exception {
        Student student = new Student();
        student.setProfno(input.getProfno());
        studentMapper.updateByProfno(student);

        int rows = professorMapper.delete(input);
        
        if (rows == 0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return rows;
    }

    @Override
    public Professor getItem(Professor input) throws ServiceNoResultException, Exception {
        Professor output = professorMapper.selectItem(input);

        if(output == null){
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return output;
    }

    @Override
    public List<Professor> getList(Professor input) throws ServiceNoResultException, Exception {
        return professorMapper.selectList(input);
    }

    @Override
    public int getCount(Professor input) throws Exception {
        int output = 0;
        try {
            output = professorMapper.selectCount(input);
        } catch (Exception e) {
            log.error("데이터 집계에 실패했습니다." + e);
            throw e;
        }
        

        return output;
    }
    
}
