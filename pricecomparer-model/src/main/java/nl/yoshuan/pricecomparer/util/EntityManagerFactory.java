package nl.yoshuan.pricecomparer.util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public final class EntityManagerFactory {

	private static javax.persistence.EntityManagerFactory entityManagerFactory = Persistence
			.createEntityManagerFactory("nl.yoshuan.persistance-unit");

	private EntityManagerFactory() {
	}

	public static EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	public static void closeEntityManagerFactory() {
		entityManagerFactory.close();
	}

}
