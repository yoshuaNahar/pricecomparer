package nl.yoshuan.pricecomparer.dbhandler;

import nl.yoshuan.pricecomparer.dao.CategoryDao;
import nl.yoshuan.pricecomparer.dao.ProductDao;
import nl.yoshuan.pricecomparer.dao.ProductVariablesDao;
import nl.yoshuan.pricecomparer.datafetcher.AhProduct;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import nl.yoshuan.pricecomparer.mappers.AhProductMapper;
import nl.yoshuan.pricecomparer.util.EntityManagerFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class DbHandler {

    private CategoryDao categoryDao;
    private ProductDao productDao;
    private ProductVariablesDao productVariablesDao;

    // For now
    private EntityManager em = EntityManagerFactory.createEntityManager();

    public DbHandler(CategoryDao categoryDao, ProductDao productDao, ProductVariablesDao productVariablesDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.productVariablesDao = productVariablesDao;

        this.categoryDao.setEntityManager(em);
        this.productDao.setEntityManager(em);
        this.productVariablesDao.setEntityManager(em);
    }

    public void addProducts(List<AhProduct> ahProducts) {
        for (AhProduct ahProduct : ahProducts) {
            AhProductMapper.mapAhProductToDbProduct(ahProduct);
        }

        for (AhProduct ahProduct : ahProducts) {
            Product product = ahProduct.getProduct();
            Category category = ahProduct.getCategory();
            ProductVariables productVariables = ahProduct.getProductVariables();

            em.getTransaction().begin();

            Category managedCategory = categoryDao.persistIfNotExist(category);
            product.setCategory(managedCategory);
            productDao.persist(product);
            productVariablesDao.persist(productVariables);

            em.getTransaction().commit();
            em.clear();
        }

        EntityManagerFactory.closeEntityManagerFactory();
    }

}
