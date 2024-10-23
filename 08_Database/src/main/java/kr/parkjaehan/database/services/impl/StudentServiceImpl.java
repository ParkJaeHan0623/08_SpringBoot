package kr.parkjaehan.database.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.mappers.StudentMapper;
import kr.parkjaehan.database.models.Student;
import kr.parkjaehan.database.services.StudentService;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentMapper studentMapper;
    
    @Override
    public Student addItem(Student input) throws ServiceNoResultException, Exception {
        int rows = studentMapper.insert(input);

        if (rows == 0) {
            throw new ServiceNoResultException("데이터 입력에 실패했습니다.");
        }

        return studentMapper.selectItem(input);
    }

    @Override
    public Student editItem(Student input) throws ServiceNoResultException, Exception {
        int rows = studentMapper.update(input);

        if (rows == 0) {
            throw new ServiceNoResultException("수정된 데이터가 없습니다.");
        }

        return studentMapper.selectItem(input);
    }

    @Override
    public int deleteItem(Student input) throws ServiceNoResultException, Exception {
        int rows = studentMapper.delete(input);

        if (rows == 0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다.");
        }

        return rows;
    }

    @Override
    public Student getItem(Student input) throws ServiceNoResultException, Exception {
        Student result = studentMapper.selectItem(input);

        if (result == null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다.");
        }

        return result;
    }

    @Override
    public List<Student> getList(Student input) throws ServiceNoResultException, Exception {
        return studentMapper.selectList(input);
    }
    
}