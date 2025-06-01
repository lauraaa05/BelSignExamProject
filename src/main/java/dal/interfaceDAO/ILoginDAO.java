package dal.interfaceDAO;

import be.Admin;
import be.Operator;
import be.QualityControl;
import be.User;
import dal.UserRole;

import java.sql.SQLException;
import java.util.List;

public interface ILoginDAO {

    boolean validateUser(String username, String password, UserRole expectedRole);

    List<User> getAllUsers();

    User addUser(User user, String password);

    boolean updateUser(User user);

    User deleteUser(User user);

    Operator getOperatorByUsername(String username) throws SQLException;

    QualityControl getQCUByUsername(String username) throws SQLException;

    Admin getAdminByUsername(String username) throws SQLException;

    int getRoleIdByName(String roleName) throws SQLException;

    UserRole getUserRole(String username, String password) throws SQLException;
}