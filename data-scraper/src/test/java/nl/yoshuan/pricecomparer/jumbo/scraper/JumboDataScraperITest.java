package nl.yoshuan.pricecomparer.jumbo.scraper;

import nl.yoshuan.pricecomparer.config.TestAppConfig;
import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
public class JumboDataScraperITest {

    @Autowired private JumboDataScraper jumboDataScraper;

    @Test
    public void scrapeFirstJumboPage() {
        List<JumboProduct> jumboProducts = jumboDataScraper.getJumboProductsFrom(1);

        assertThat(jumboProducts.size(), is(not(0)));
    }

}
