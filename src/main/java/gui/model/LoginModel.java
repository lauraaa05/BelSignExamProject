package gui.model;

import be.User;
import bll.LoginManager;
import exceptions.BLLException;

public class LoginModel {

    private final LoginManager loginManager = new LoginManager();

    public User login(String username, String password) throws BLLException {
        return loginManager.login(username, password);
    }
}