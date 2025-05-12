package bll;

import dal.LoginDAO;

public class LoginManager {

    private final LoginDAO lgn = new LoginDAO();

    public boolean checkQCULogin(String username, String password) {
        return lgn.validateQualityControlUser(username, password);
    }

    public boolean checkOperatorLogin(String username, String password) {
        return lgn.validateOperatorUser(username, password);
    }

    public boolean checkAdminLogin(String username, String password) {
        return lgn.validateAdminUser(username, password);
    }

}

