package gui.model;

import be.Operator;
import bll.LoginManager;
import dal.LoginDAO;

public class LoginModel {

    private final LoginManager loginManager = new LoginManager();
    private final LoginDAO loginDAO = new LoginDAO();

    public boolean loginAsOperator(String username, String password) {
        return loginManager.checkOperatorLogin(username, password);
    }

    public Operator getOperator(String username) {
        return loginDAO.getOperatorByUsername(username);
    }
}