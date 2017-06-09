package nl.yoshuan.pricecomparer.jumbo.internal;

import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class JumboDataScraperUTest {

    private static final String HTML_FILE_LOCATION = "jumbo-sample-data.html";
    private JumboDataScraper jumboDataScraper;

    @Before
    public void initDataParser() {
        jumboDataScraper = new JumboDataScraper(HTML_FILE_LOCATION);
    }

    @Test
    public void readLocalData_checkNotNull() {
        assertThat(jumboDataScraper.readLocalFile(), is(notNullValue()));
    }

    @Test
    public void getElement_checkWorking() {
        assertThat(jumboDataScraper.getFirstProductRaw(jumboDataScraper.readLocalFile()),
                is(notNullValue()));
    }

    @Test
    public void getProducts_checkWorking() {
        List<JumboProduct> products = jumboDataScraper.getProducts(jumboDataScraper.readLocalFile());

        assertThat(products.get(0).getName(), is("Jumbo Aardbeien Hollands 500g"));
        assertThat(products.size(), is(12));
    }

    @Test
    public void findBonusTypeImageFromLink() {
        String url = "https://www.jumbo.com/INTERSHOP/web/WFS/Jumbo-Grocery-Site/nl_NL/-/EUR/ViewPromotionAttachment-OpenFile;pgid=656nyL2z7u5SRpdIf9Zv96c40000XLQXg5AB;sid=VGLo6lzpITTU6gQjsar09j_j3PlfWdRy49FZW6wk?LocaleId=&DirectoryPath=Jaaraanbiedingen2017%2FPrijs-badge&FileName=Jumbo-Jaaraanbiedingen-badge-M-2v3.png&UnitName=Jumbo-Grocery";
        String imageName = jumboDataScraper.getBonusTypeImageFromSrc(url);

        assertThat(imageName, is("Jumbo-Jaaraanbiedingen-badge-M-2v3.png"));
    }

}
