package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.config.TestDbConfig;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static nl.yoshuan.pricecomparer.entities.ProductVariables.Supermarket.AH;
import static nl.yoshuan.pricecomparer.utils.CategoryUtil.createFirstChildCategory;
import static nl.yoshuan.pricecomparer.utils.CategoryUtil.createParentCategory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDbConfig.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductVariablesDaoITest {

    @Autowired private ProductVariablesDao productVariablesDao;
    @Autowired private ProductDao productDao;
    @Autowired private CategoryDao categoryDao;

    @Before
    public void setup() {
        loadProductAndCategory();
    }

    @Test
    public void addProductVariablesToProduct() {
        ProductVariables productVariables =
                new ProductVariables("src", null, 100, 0, null, null, AH, "productIcon", new Date(), null);
        Product product = productDao.findById(1L);
        productVariables.setProduct(product);

        productVariablesDao.persist(productVariables);

        assertThat(productVariablesDao.getCount(), is(1L));

        ProductVariables managedProductVariables = productVariablesDao.findById(1L);

        assertThat(managedProductVariables.getProductSrc(), is("src"));
        assertThat(managedProductVariables.getProduct().getName(), is("tomaat"));
    }


    private void loadProductAndCategory() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(parentCategory);
        Product product = new Product("tomaat", "500g", "AH", childCategory, null);

        categoryDao.persist(parentCategory);
        categoryDao.persist(childCategory);
        productDao.persist(product);
    }

}
