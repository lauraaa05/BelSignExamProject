package bll;

import be.User;
import dal.LoginDAO;
import dal.UserRole;
import exceptions.BLLException;

import java.sql.SQLException;
import java.util.List;

public class LoginManager {

    private final LoginDAO lgn = new LoginDAO();

    public User login(String username, String password) throws BLLException {
        try {
            UserRole role = lgn.getUserRole(username, password);
            if (role == null) return null;

            return switch (role) {
                case OPERATOR -> lgn.getOperatorByUsername(username);
                case QUALITY_CONTROL -> lgn.getQCUByUsername(username);
                case ADMIN -> lgn.getAdminByUsername(username);
            };
        } catch (SQLException e) {
            throw new BLLException("Failed to log in due to a database error", e);
        }
    }

    public User addUser(User user, String password) {
        return lgn.addUser(user, password);
    }

    public List<User> getAllUsers() {
        return lgn.getAllUsers();
    }

    public User deleteUser (User user) {
        return lgn.deleteUser(user);
    }

    public boolean updateUser (User user) {
        return lgn.updateUser(user);
    }
}