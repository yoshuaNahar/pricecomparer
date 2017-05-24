package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import nl.yoshuan.pricecomparer.utils.DaoTestSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static nl.yoshuan.pricecomparer.entities.ProductVariables.Supermarket.AH;
import static nl.yoshuan.pricecomparer.utils.CategoryUtil.createFirstChildCategory;
import static nl.yoshuan.pricecomparer.utils.CategoryUtil.createParentCategory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProductVariablesDaoUTest extends DaoTestSetup {

    private ProductVariablesDao productVariablesDao;

    @Before
    public void setup() {
        initializeTestDB();

        productVariablesDao = new ProductVariablesDaoImpl();
        productVariablesDao.setEntityManager(em);

        loadProductAndCategory();
    }

    @After
    public void cleanUp() {
        closeEntityManager();
    }

    @Test
    public void addProductVariablesToProduct() {
        ProductVariables productVariables = new ProductVariables("src", "imgSrc", 100, 0, null, null, AH, "productIcon", new Date());
        Product product = em.getReference(Product.class, 1L);
        productVariables.setProduct(product);

        dbCommandExecutor.executeCommand(() -> productVariablesDao.persist(productVariables));
        ProductVariables managedProductVariables = dbCommandExecutor.executeCommand(() -> productVariablesDao.findById(1L));

        assertThat(managedProductVariables.getProductSrc(), is("src"));
        assertThat(managedProductVariables.getProduct().getName(), is("tomaat"));
    }

    private void loadProductAndCategory() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(parentCategory);
        Product product = new Product("tomaat", "500g", "AH", childCategory);

        dbCommandExecutor.executeCommand(() -> {
            em.persist(parentCategory);
            em.persist(childCategory);
            em.persist(product);
            return null;
        });
    }

}
