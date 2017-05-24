package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.utils.DaoTestSetup;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static nl.yoshuan.pricecomparer.entities.ProductVariables.Supermarket.AH;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProductVariablesDaoUTest extends DaoTestSetup {

    private ProductVariablesDao productVariablesDao;

    @Before
    public void setup() {
        initializeTestDB();

        productVariablesDao = new ProductVariablesDaoImpl();
        productVariablesDao.setEntityManager(em);
    }

    @Test
    public void addProductVariablesToProduct() {
        loadProductAndCategory();
        ProductVariables productVariables = new ProductVariables("src", "imgSrc", 100, 0, null, null, AH, "productIcon", new Date());
        Product product = em.getReference(Product.class, 1L);
        productVariables.setProduct(product);
        dbCommandExecutor.executeCommand(() -> productVariablesDao.persist(productVariables));

        assertThat(productVariablesDao.findById(1L).getProductSrc(), is("src"));
    }

    private void loadProductAndCategory() {
        Category category = new Category("groente, fruit en aardappelen", null);
        Product product = new Product("tomaat", "500g", "AH", category);
        dbCommandExecutor.executeCommand(() -> {
            em.persist(product);
            return null;
        });
    }

    @After
    public void cleanUp() {
        closeEntityManager();
    }

}
