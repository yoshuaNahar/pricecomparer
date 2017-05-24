package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.utils.DaoTestSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static nl.yoshuan.pricecomparer.utils.CategoryUtil.GROENTE;
import static nl.yoshuan.pricecomparer.utils.CategoryUtil.createFirstChildCategory;
import static nl.yoshuan.pricecomparer.utils.CategoryUtil.createParentCategory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProductDaoUTest extends DaoTestSetup {

    private ProductDao productDao;

    @Before
    public void setup() {
        initializeTestDB();

        productDao = new ProductDaoImpl();
        productDao.setEntityManager(em);

        loadCategories();
    }

    @After
    public void cleanUp() {
        closeEntityManager();
    }

    @Test
    public void addProductAndFindIt() {
        Category managedCategory = em.find(Category.class, 2L);
        Product product = new Product("tomaat", "500g", "AH", managedCategory);

        Product managedProduct = dbCommandExecutor.executeCommand(() -> productDao.persist(product));

        assertThat(productDao.getCount(), is(1L));
        assertThat(managedProduct.getName(), is("tomaat"));
        assertThat(managedProduct.getCategory().getCategoryName(), is(GROENTE));
    }

    @Test
    public void addProductAndFindItByProperty() {
        Category managedCategory = em.find(Category.class, 2L);
        Product product = new Product("tomaat", "500g", "AH", managedCategory);

        dbCommandExecutor.executeCommand(() -> productDao.persist(product));

        Product managedProduct = productDao.findByPropertyValue("name", "tomaat").get(0);

        assertThat(productDao.getCount(), is(1L));
        assertThat(managedProduct.getId(), is(1L));
        assertThat(managedProduct.getName(), is("tomaat"));
        assertThat(managedProduct.getCategory().getCategoryName(), is(GROENTE));
    }

    private void loadCategories() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(parentCategory);

        dbCommandExecutor.executeCommand(() -> {
            em.persist(parentCategory);
            em.persist(childCategory);
            return null;
        });
    }

}
