package nl.yoshuan.pricecomparer.daos;

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

    E merge(E entity);

    void makeDetached(E entity);

    void clearPersistenceContext();

}
