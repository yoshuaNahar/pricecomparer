package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.utils.DaoTestSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        Category managedCategory = em.find(Category.class, 1L);
        Product product = new Product("tomaat", "500g", "AH", managedCategory);

        dbCommandExecutor.executeCommand(() -> productDao.persist(product));

        assertThat(productDao.getCount(), is(1L));
        assertThat(productDao.findById(1L).getName(), is("tomaat"));
    }

    @Test
    public void addProductWithCategoryAndFindIt() {
        Category newCategory = new Category("fruit", createParentCategory());
        Product product = new Product("tomaat", "500g", "AH", newCategory);

        dbCommandExecutor.executeCommand(() -> productDao.persist(product));

        assertThat(productDao.getCount(), is(1L));
        assertThat(productDao.findById(1L).getName(), is("tomaat"));
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
