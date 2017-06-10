package nl.yoshuan.pricecomparer.jumbo.dbhandler;

import nl.yoshuan.pricecomparer.config.TestAppConfig;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.SimpleProduct;
import nl.yoshuan.pricecomparer.jumbo.util.LuceneHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
public class LuceneHelperTest {

    @Autowired private LuceneHelper luceneHelper;

    @Before
    public void addSomeDataToIndex() {
        List<SimpleProduct> simpleProducts = new ArrayList<>();
        simpleProducts.add(new SimpleProduct("AH Tomaten", 1L));
        simpleProducts.add(new SimpleProduct("AH Trostomaten", 1L));
        simpleProducts.add(new SimpleProduct("AH Komkommer", 2L));

        luceneHelper.addDbDataIntoLuceneIndexes(simpleProducts);
    }

    @Test
    public void getCorrectCategoryMatch() {
        Product product = new Product("Jumbo Trostomaten", null, null, null, null);
        luceneHelper.insertClosestCategoryMatchToProduct(product);

        assertThat(product.getCategory().getId(), is(1L));
    }

}
