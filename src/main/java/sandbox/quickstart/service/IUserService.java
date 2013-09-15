package sandbox.quickstart.service;

import jabara.general.NotFound;
import jabara.general.Sort;

import java.util.List;

import sandbox.quickstart.entity.EUser;
import sandbox.quickstart.service.impl.UserServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * 
 */
@ImplementedBy(UserServiceImpl.class)
public interface IUserService {

    /**
     * @param pId -
     * @return -
     * @throws NotFound -
     */
    EUser search(long pId) throws NotFound;

    /**
     * @param pSort ソート条件.
     * @return 全件.
     */
    List<EUser> getAll(Sort pSort);

    /**
     * 
     */
    void insertAdministratorIfNotExists();
}
