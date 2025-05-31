package bll;

import be.Operator;
import dal.OperatorDAO;

import java.util.HashMap;
import java.util.Map;

public class OperatorManager{

    private final OperatorDAO operatorDAO = new OperatorDAO();

    public Operator getOperatorByUserId(int userId){
        return operatorDAO.getOperatorById(userId);
    }
}