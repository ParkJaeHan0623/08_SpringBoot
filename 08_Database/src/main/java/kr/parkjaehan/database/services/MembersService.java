package kr.parkjaehan.database.services;
import kr.parkjaehan.database.exceptions.ServiceNoResultException;
import kr.parkjaehan.database.models.Members;

import java.util.List;

public interface MembersService {
    public Members addItem(Members input) throws ServiceNoResultException, Exception;

    public Members editItem(Members input) throws ServiceNoResultException, Exception;

    public int deleteItem(Members input) throws ServiceNoResultException, Exception;

    public Members getItem(Members input) throws ServiceNoResultException, Exception;

    public List<Members> getList(Members input) throws ServiceNoResultException, Exception;
}
