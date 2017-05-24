package nl.yoshuan.pricecomparer.dao;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class GenericDaoImpl<E, ID> implements GenericDao<E, ID> {

    protected final Class<E> entityClass;

    // @Autowired // For the future
    protected EntityManager em;

    protected GenericDaoImpl(Class<E> entityClass) {
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
    public List<E> findByPropertyValue(String column, String columnValue) {
        return em
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + column + " = :columnValue", entityClass)
                .setParameter("columnValue", columnValue)
                .getResultList();
    }

    @Override
    public List<E> findAll(String orderColumn) {
        return em
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e ORDER BY e." + orderColumn, entityClass)
                .getResultList();
    }

    @Override
    public Long getCount() {
        CriteriaQuery<Long> c = em.getCriteriaBuilder().createQuery(Long.class);
        c.select(em.getCriteriaBuilder().count(c.from(entityClass)));
        return em.createQuery(c).getSingleResult();
    }

    @Override
    public E persist(E entity) {
        em.persist(entity);
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
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.id = :id")
                .setParameter("id", id)
                .getResultList().size() == 1;
    }

    // There should only be one. That is why I don't use SELECT 1 FROM or getSingleResult or getFirstResult
    @Override
    public boolean existsByPropertyValue(String column, String columnValue) {
        return em
                .createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + column + " = :columnValue")
                .setParameter("columnValue", columnValue)
                .getResultList().size() == 1;
    }

    @Override
    public void makeTransient(E entity) {
        em.remove(entity);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    // Till I implement DI
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

}
