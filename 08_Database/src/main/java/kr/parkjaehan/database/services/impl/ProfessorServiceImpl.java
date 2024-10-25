package kr.parkjaehan.database.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.mappers.ProfessorMapper;
import kr.parkjaehan.database.mappers.StudentMapper;
import kr.parkjaehan.database.models.Professor;
import kr.parkjaehan.database.models.Student;
import kr.parkjaehan.database.services.ProfessorService;

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
    
}
