package bll;

import be.Admin;
import be.Operator;
import be.QualityControl;
import be.User;
import dal.LoginDAO;
import dal.UserRole;

import java.sql.SQLException;

public class LoginManager {

    private final LoginDAO lgn = new LoginDAO();

    public User login(String username, String password) {
        try {
            UserRole role = lgn.getUserRole(username, password);
            if (role == null) return null;

            return switch (role) {
                case OPERATOR -> lgn.getOperatorByUsername(username);
                case QUALITY_CONTROL -> lgn.getQCUByUsername(username);
                case ADMIN -> lgn.getAdminByUsername(username);
            };
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public QualityControl getQCUByUsername(String username) {
        return lgn.getQCUByUsername(username);
    }

    public Admin getAdminByUsername(String username) {
        return lgn.getAdminByUsername(username);
    }

    public Operator getOperatorByUsername(String username) {
        return lgn.getOperatorByUsername(username);
    }
}