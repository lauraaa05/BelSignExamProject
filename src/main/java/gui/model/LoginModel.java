package gui.model;

import be.Operator;
import be.User;
import bll.LoginManager;
import dal.LoginDAO;

public class LoginModel {

    private final LoginManager loginManager = new LoginManager();
    private final LoginDAO loginDAO = new LoginDAO();

    public User login(String username, String password) {
        return loginManager.login(username, password);
    }

    public Operator getOperator(String username) {
        return loginDAO.getOperatorByUsername(username);
    }
}