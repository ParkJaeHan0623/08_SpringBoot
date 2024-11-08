package kr.parkjaehan.myshop.services;
import kr.parkjaehan.myshop.exceptions.ServiceNoResultException;
import kr.parkjaehan.myshop.models.Members;

import java.util.List;

public interface MembersService {
    public Members addItem(Members input) throws ServiceNoResultException, Exception;

    public Members editItem(Members input) throws ServiceNoResultException, Exception;

    public int deleteItem(Members input) throws ServiceNoResultException, Exception;

    public Members getItem(Members input) throws ServiceNoResultException, Exception;

    public List<Members> getList(Members input) throws ServiceNoResultException, Exception;

    public void isUniqueUserId(String userId) throws Exception;

    public void isUniqueEmail(String email) throws Exception;

    public Members findId(Members input) throws Exception;
}
        
