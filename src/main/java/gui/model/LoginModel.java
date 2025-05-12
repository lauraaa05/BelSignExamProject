package gui.model;

import bll.LoginManager;

public class LoginModel {

    private final LoginManager loginManager = new LoginManager();

    public boolean loginAsOperator(String username, String password) {
        return loginManager.checkOperatorLogin(username, password);
    }
}
