package gui.model;

import be.Operator;
import be.User;
import bll.LoginManager;
import dal.LoginDAO;
import exceptions.BLLException;

import java.sql.SQLException;

public class LoginModel {

    private final LoginManager loginManager = new LoginManager();
    private final LoginDAO loginDAO = new LoginDAO();

    public User login(String username, String password) throws BLLException {
        return loginManager.login(username, password);
    }

    public Operator getOperator(String username) throws SQLException {
        return loginDAO.getOperatorByUsername(username);
    }
}