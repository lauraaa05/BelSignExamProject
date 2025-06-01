package dal.interfaceDAO;

import be.Picture;
import exceptions.DALException;

import java.util.List;

public interface IPictureDAO {

    void savePicture(Picture picture) throws DALException;

    List<Picture> getPicturesByOrderNumber(String orderNumber) throws DALException;

    int countImagesForOrderNumber(String orderNumber) throws DALException;

    List<String> getTakenSidesForOrderNumber(String orderNumber) throws DALException;

    List<Picture> getPicturesByOrderNumberRaw(String orderNumber) throws DALException;

    void deletePictureById(int imageId) throws DALException;
}
