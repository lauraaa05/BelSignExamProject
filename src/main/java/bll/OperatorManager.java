package bll;

import be.Operator;
import dal.OperatorDAO;
import exceptions.BLLException;
import exceptions.DALException;


public class OperatorManager{

    private final OperatorDAO operatorDAO = new OperatorDAO();

    public Operator getOperatorByUserId(int userId) throws BLLException {
        try {
            return operatorDAO.getOperatorById(userId);
        } catch (DALException e) {
            throw new BLLException("Failed to retrieve operator.", e);
        }
    }
}