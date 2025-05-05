package bll;

import dal.LoginDAO;

public class LoginManager {

    private final LoginDAO lgn = new LoginDAO();

    public boolean checkQCULogin(String username, String password) {
        return lgn.validateQualityControlUser(username, password);
    }

}
