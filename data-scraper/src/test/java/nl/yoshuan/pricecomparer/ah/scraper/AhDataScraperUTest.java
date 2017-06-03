package nl.yoshuan.pricecomparer.ah.scraper;

import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.config.TestAppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
public class AhDataScraperUTest {

    @Autowired private AhDataScraper ahDataScraper;

    @Test
    public void getAhProductsFromDeepestSubCategory() {
        List<AhProduct> ahProducts = ahDataScraper.getAllAhProductsFrom("/producten/aardappel-groente-fruit/groente/tomaat-paprika-mais/tomaten/trostomaten");

        AhProduct firstProduct = ahProducts.get(0);

        assertThat(ahProducts.size(), is(4));
        assertThat(firstProduct.getName(), is("AH Trosto\u00ADma\u00ADten"));
    }

    @Test
    public void getAhProductsFromSecondDeepestSubCategory() {
        List<AhProduct> ahProducts = ahDataScraper.getAllAhProductsFrom("/producten/aardappel-groente-fruit/groente/tomaat-paprika-mais/tomaten/");

        AhProduct firstProduct = ahProducts.get(0);

        assertThat(ahProducts.size(), is(20));
        assertThat(firstProduct.getName(), is("AH Trosto\u00ADma\u00ADten"));
    }

}
