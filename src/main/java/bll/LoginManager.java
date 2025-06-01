package bll;

import be.User;
import dal.LoginDAO;
import dal.UserRole;
import exceptions.BLLException;
import exceptions.DALException;

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
        } catch (DALException e) {
            throw new BLLException("Failed to log in due to a database error", e);
        }
    }

    public User addUser(User user, String password) throws BLLException {
        try {
            return lgn.addUser(user, password);
        } catch (DALException e) {
            throw new BLLException("Failed to add user.", e);
        }
    }

    public List<User> getAllUsers() throws BLLException {
        try {
            return lgn.getAllUsers();
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve all users.", e);
        }
    }

    public User deleteUser (User user) {
        return lgn.deleteUser(user);
    }

    public boolean updateUser (User user) throws BLLException {
        try {
            return lgn.updateUser(user);
        } catch (DALException e) {
            throw new BLLException("Failed to update user.", e);
        }
    }
}