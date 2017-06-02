package nl.yoshuan.pricecomparer.dao;

import java.util.List;

public interface GenericDao<E, ID> {

    E findById(ID id);

    E findReferenceById(ID id);

    E findByUniquePropertyValue(String column, String columnValue);

    List<E> findByPropertyValue(String column, String columnValue);

    List<E> findAll(String orderColumn);

    Long getCount();

    boolean existsById(ID id);

    boolean existsByUniquePropertyValue(String column, String columnValue);

    E persist(E entity);

    E persistIfNotExist(E entity);

    E merge(E entity);

    void makeTransient(E entity);

}
