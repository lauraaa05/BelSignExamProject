package dal.interfaceDAO;

import be.Admin;
import be.Operator;
import be.QualityControl;
import be.User;
import dal.UserRole;
import exceptions.DALException;

import java.sql.SQLException;
import java.util.List;

public interface ILoginDAO {

    boolean validateUser(String username, String password, UserRole expectedRole) throws DALException;

    List<User> getAllUsers() throws DALException;

    User addUser(User user, String password) throws DALException;

    boolean updateUser(User user) throws DALException;

    User deleteUser(User user) throws DALException;

    Operator getOperatorByUsername(String username) throws DALException;

    QualityControl getQCUByUsername(String username) throws DALException;

    Admin getAdminByUsername(String username) throws DALException;

    int getRoleIdByName(String roleName) throws DALException;

    UserRole getUserRole(String username, String password) throws DALException;
}