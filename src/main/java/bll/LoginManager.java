package bll;

import be.Admin;
import be.QualityControl;
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

    public QualityControl getQCUByUsername(String username) {
        return lgn.getQCUByUsername(username);
    }

    public Admin getAdminByUsername(String username) {
        return lgn.getAdminByUsername(username);
    }
}

