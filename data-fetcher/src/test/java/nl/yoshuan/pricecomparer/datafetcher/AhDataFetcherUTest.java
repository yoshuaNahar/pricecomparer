package nl.yoshuan.pricecomparer.datafetcher;

import org.junit.Before;
import org.junit.Test;

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
        ahDataFetcher.goToFirstInnerMostPage("/producten/aardappel-groente-fruit/groente/tomaat-komkommer");
    }
    
}
