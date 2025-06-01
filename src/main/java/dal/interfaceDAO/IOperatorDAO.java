package dal.interfaceDAO;

import be.Operator;
import exceptions.DALException;

public interface IOperatorDAO {
    Operator getOperatorById(int userId) throws DALException;
}