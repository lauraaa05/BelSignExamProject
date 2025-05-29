package dal.interfaceDAO;

import java.util.List;

public interface IBaseDAO <T>{
    void save(T obj);
    void update(T obj);
    void delete(T obj);
    T findById(int id);
    List<T> findAll();
    //IF you don't need one of these  just return null
    //This is called repository pattern
}
