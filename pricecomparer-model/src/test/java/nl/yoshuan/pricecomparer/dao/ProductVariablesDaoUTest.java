package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.config.TestConfig;
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
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductVariablesDaoUTest {

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
                new ProductVariables("src", "imgSrc", 100, 0, null, null, AH, "productIcon", new Date());
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
        parentCategory.getChildCategories().add(childCategory);
        Product product = new Product("tomaat", "500g", "AH", childCategory);

        categoryDao.persist(parentCategory);
        productDao.persist(product);
    }

}
