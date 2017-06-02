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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

//        this.categoryDao.setEntityManager(em);
//        this.productDao.setEntityManager(em);
//        this.productVariablesDao.setEntityManager(em);
    }

    // so that I can test
    static List<Category> splitFullCategory(String fullCategoryName) {
        // the full category will usually be of this type, so I remove the first producten substring
        // /producten/aardappel-groente-fruit/groente/tomaat-komkommer/tomaten/trostomaten

        String fullCategoryNameExceptProducten = fullCategoryName.replace("/producten/", "");
        String[] categoryNames = fullCategoryNameExceptProducten.split(Pattern.quote("/")); // split on /

        List<Category> linkedCategories = new ArrayList<>();

        for (String categoryName : categoryNames) {
            linkedCategories.add(new Category(categoryName, null));
        }
        int linkedCategoriesSize = linkedCategories.size();
//        for (int i = 1; i < linkedCategoriesSize; i++) { // start with second index, because first category has no parent
//            linkedCategories.get(i).setParentCategory(linkedCategories.get(i - 1));
//        }

        return linkedCategories;
    }

    public void addProducts(List<AhProduct> ahProducts) {
        for (AhProduct ahProduct : ahProducts) {
            AhProductMapper.mapAhProductToDbProduct(ahProduct);
        }

        List<Category> linkedCategories = splitFullCategory(ahProducts.get(0).getFullCategoryName());
        persistLinkedCategories(linkedCategories);
        int lastCategory = linkedCategories.size() - 1;

        for (AhProduct ahProduct : ahProducts) {
            Product product = ahProduct.getProduct();
            Category category = linkedCategories.get(lastCategory);
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

    private void persistLinkedCategories(List<Category> linkedCategories) {
        linkedCategories.forEach(category -> System.out.println(category.getCategoryName()));

        em.getTransaction().begin();

        int linkedCategoriesSize = linkedCategories.size();
        for (int i = 0; i < linkedCategoriesSize; i++) {
            if (i > 0) {
                linkedCategories.get(i).setParentCategory(linkedCategories.get(i - 1));
            }
            linkedCategories.set(i, categoryDao.persistIfNotExist(linkedCategories.get(i)));
        }

        em.getTransaction().commit();
    }

}
