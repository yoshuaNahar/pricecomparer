package nl.yoshuan.pricecomparer.utils;

import org.junit.Ignore;

import javax.persistence.EntityManager;

@Ignore
public class DbCommandTransactionalExecutor {

    private EntityManager em;

    public DbCommandTransactionalExecutor(EntityManager em) {
        this.em = em;
    }

    public <T> T executeCommand(DbCommand<T> dbCommand) {
        try {
            em.getTransaction().begin();
            T toReturn = dbCommand.execute();
            em.getTransaction().commit();
            em.clear();
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new IllegalStateException(e);
        }
    }

}
