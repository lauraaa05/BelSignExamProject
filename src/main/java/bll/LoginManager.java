package bll;

import be.Admin;
import be.Operator;
import be.QualityControl;
import be.User;
import dal.LoginDAO;
import dal.UserRole;
import exceptions.BLLException;

import java.sql.SQLException;

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

    public QualityControl getQCUByUsername(String username) throws BLLException {
        try {
            return lgn.getQCUByUsername(username);
        } catch (SQLException e) {
            throw new BLLException("Failed to fetch Quality Control user", e);
        }
    }

    public Admin getAdminByUsername(String username) throws BLLException {
        try {
            return lgn.getAdminByUsername(username);
        } catch (SQLException e) {
            throw new BLLException("Failed to fetch Admin user", e);
        }
    }

    public Operator getOperatorByUsername(String username) throws BLLException {
        try {
            return lgn.getOperatorByUsername(username);
        } catch (SQLException e) {
            throw new BLLException("Failed to fetch Operator user", e);
        }
    }
}