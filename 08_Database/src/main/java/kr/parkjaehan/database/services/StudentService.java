package kr.parkjaehan.database.services;

import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.models.Student;
import java.util.List;

public interface StudentService {

    public Student addItem(Student input) throws ServiceNoResultException, Exception;

    public Student editItem(Student input) throws ServiceNoResultException, Exception;

    public int deleteItem(Student input) throws ServiceNoResultException, Exception;

    public Student getItem(Student input) throws ServiceNoResultException, Exception;

    public List<Student> getList(Student input) throws ServiceNoResultException, Exception;

    public int getCount(Student input) throws Exception;
}