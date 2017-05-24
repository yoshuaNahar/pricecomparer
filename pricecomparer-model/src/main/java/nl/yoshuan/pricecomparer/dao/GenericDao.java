package nl.yoshuan.pricecomparer.dao;

import javax.persistence.EntityManager;
import java.util.List;

public interface GenericDao<E, ID> {

	E findById(ID id);

	E findReferenceById(ID id);

	List<E> findAll(String orderColumn);

	Long getCount();

	boolean existsById(ID id);

	// Should probably only be used when the column is unique, like in the category table or the product table
	boolean existsByPropertyValue(String column, String name);

	// Also updates, because of dirty checking
	E persist(E entity);

	E persistIfNotExist(E entity);

	E merge(E entity);

	void makeTransient(E entity);

	EntityManager getEntityManager();

	// Remove once I have Dependency Injection
	void setEntityManager(EntityManager em);

}
