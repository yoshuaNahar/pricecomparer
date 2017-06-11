package nl.yoshuan.pricecomparer.daos;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class GenericDaoImpl<E, ID> implements GenericDao<E, ID> {

    final Class<E> entityClass;
    @PersistenceContext EntityManager em;

    GenericDaoImpl(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public E findById(ID id) {
        if (id == null) {
            return null;
        }
        return em.find(entityClass, id);
    }

    @Override
    public E findReferenceById(ID id) {
        if (id == null) {
            return null;
        }
        return em.getReference(entityClass, id);
    }

    @Override
    public E findByUniquePropertyValue(String column, String columnValue) {
        try {
            return em
                    .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e"
                            + " WHERE e." + column + " = :columnValue", entityClass)
                    .setParameter("columnValue", columnValue)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<E> findByPropertyValue(String column, String columnValue) {
        return em
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e"
                        + " WHERE e." + column + " = :columnValue", entityClass)
                .setParameter("columnValue", columnValue)
                .getResultList();
    }

    @Override
    public List<E> findAll(String orderColumn) {
        return em
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e"
                        + " ORDER BY e." + orderColumn, entityClass)
                .getResultList();
    }

    @Override
    // testing out criteriaBuilder
    public Long getCount() {
        CriteriaQuery<Long> c = em.getCriteriaBuilder().createQuery(Long.class);
        c.select(em.getCriteriaBuilder().count(c.from(entityClass)));
        return em.createQuery(c).getSingleResult();
    }

    @Override
    public E persist(E entity) {
        em.persist(entity);
        em.flush(); // Why doesn't Spring transaction automatically add the em.flush inside the transaction? Or why isn't Hibernate AUTO flush active?
        return entity;
    }

    @Override
    public E merge(E entity) {
        return em.merge(entity);
    }

    // There should only be one. That is why I don't use SELECT 1 FROM or getSingleResult or getFirstResult
    @Override
    public boolean existsById(ID id) {
        return em
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e"
                        + " WHERE e.id = :id")
                .setParameter("id", id)
                .getResultList().size() == 1;
    }

    // Should probably only be used when the column is unique, like in the category table for the name or in the product table
    @Override
    public boolean existsByUniquePropertyValue(String column, String columnValue) {
        return em
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e"
                        + " WHERE e." + column + " = :columnValue")
                .setParameter("columnValue", columnValue)
                .getResultList().size() == 1;
    }

    @Override
    public void makeDetached(E entity) {
        em.detach(entity);
    }

    public void clearPersistenceContext() {
        em.clear();
    }

}
