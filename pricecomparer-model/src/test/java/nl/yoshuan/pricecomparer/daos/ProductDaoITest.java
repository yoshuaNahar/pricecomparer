package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.config.TestDbConfig;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static nl.yoshuan.pricecomparer.utils.CategoryUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDbConfig.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductDaoITest {

    @Autowired private ProductDao productDao;
    @Autowired private CategoryDao categoryDao;

    @Before
    public void setup() {
        loadCategories();
    }

    @Test
    public void addProductAndFindIt() {
        Category managedCategory = categoryDao.findById(2L);
        Product product = new Product("tomaat", "500g", "AH", managedCategory, null);

        productDao.clearPersistenceContext();

        Product managedProduct = productDao.persist(product);

        assertThat(productDao.getCount(), is(1L));
        assertThat(managedProduct.getName(), is("tomaat"));
        assertThat(managedProduct.getCategory().getName(), is(GROENTE));
    }

    @Test
    public void addProductAndFindItByProperty() {
        Category managedCategory = categoryDao.findById(2L);
        System.out.println(managedCategory.getId());
        Product product = new Product("tomaat", "500g", "AH", managedCategory, null);

        productDao.persist(product);

        productDao.clearPersistenceContext();

        assertThat(productDao.getCount(), is(1L));

        Product managedProduct = productDao.findByUniquePropertyValue("name", "tomaat");

        assertThat(managedProduct.getId(), is(1L));
        assertThat(managedProduct.getName(), is("tomaat"));
        assertThat(managedProduct.getCategory().getName(), is(GROENTE));
    }

    private void loadCategories() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(parentCategory);

        categoryDao.persist(parentCategory);
        categoryDao.persist(childCategory);

        categoryDao.clearPersistenceContext();
    }

}
