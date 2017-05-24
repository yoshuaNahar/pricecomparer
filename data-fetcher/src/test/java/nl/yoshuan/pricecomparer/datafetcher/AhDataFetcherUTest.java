package nl.yoshuan.pricecomparer.datafetcher;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AhDataFetcherUTest {

    private AhDataFetcher ahDataFetcher;

    @Before
    public void initAhDataFetcher() {
        ahDataFetcher = new AhDataFetcher();
    }

    @Test
    public void goToCategoryPage() {
        ahDataFetcher.goToCategoryPage("aardappel-groente-fruit");
    }

    @Test
    public void goToSubCategoryPage() {
        List<AhProduct> ahProducts = ahDataFetcher.goToFirstInnerMostPage("/producten/aardappel-groente-fruit/groente/tomaat-komkommer/tomaten/trostomaat");

        AhProduct firstProduct = ahProducts.get(0);

        assertThat(ahProducts.size(), is(3));
        assertThat(firstProduct.getName(), is("AH Trosto\u00ADma\u00ADten"));
    }
    
}
