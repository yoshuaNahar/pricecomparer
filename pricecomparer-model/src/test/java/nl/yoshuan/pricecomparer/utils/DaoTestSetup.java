package nl.yoshuan.pricecomparer.utils;

import org.junit.Ignore;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Ignore
public class DaoTestSetup {

    protected EntityManager em;
    protected DbCommandTransactionalExecutor dbCommandExecutor;
    private EntityManagerFactory emf;

    protected void initializeTestDB() {
        emf = Persistence.createEntityManagerFactory("nl.yoshuan.persistance-unit");
        em = emf.createEntityManager();
        dbCommandExecutor = new DbCommandTransactionalExecutor(em);
    }

    protected void closeEntityManager() {
        em.close();
        emf.close();
    }

}
