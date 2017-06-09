package nl.yoshuan.pricecomparer.jumbo.internal;

import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class JumboDataParserUTest {

    private static final String HTML_FILE_LOCATION = "jumbo-sample-data.html";
    private JumboDataParser jumboDataParser;

    @Before
    public void initDataParser() {
        jumboDataParser = new JumboDataParser(HTML_FILE_LOCATION);
    }

    @Test
    public void readLocalData_checkNotNull() {
        assertThat(jumboDataParser.readLocalFile(), is(notNullValue()));
    }

    @Test
    public void getElement_checkWorking() {
        assertThat(jumboDataParser.getFirstProductRaw(jumboDataParser.readLocalFile()),
                is(notNullValue()));
    }

    @Test
    public void getProducts_checkWorking() {
        List<JumboProduct> products = jumboDataParser.getProducts(jumboDataParser.readLocalFile());

        assertThat(products.get(0).getName(), is("Jumbo Aardbeien Hollands 500g"));
        assertThat(products.size(), is(12));
    }

    @Test
    public void findBonusTypeImageFromLink() {
        String url = "https://www.jumbo.com/INTERSHOP/web/WFS/Jumbo-Grocery-Site/nl_NL/-/EUR/ViewPromotionAttachment-OpenFile;pgid=656nyL2z7u5SRpdIf9Zv96c40000XLQXg5AB;sid=VGLo6lzpITTU6gQjsar09j_j3PlfWdRy49FZW6wk?LocaleId=&DirectoryPath=Jaaraanbiedingen2017%2FPrijs-badge&FileName=Jumbo-Jaaraanbiedingen-badge-M-2v3.png&UnitName=Jumbo-Grocery";
        String imageName = jumboDataParser.getBonusTypeImageFromSrc(url);

        assertThat(imageName, is("Jumbo-Jaaraanbiedingen-badge-M-2v3.png"));
    }

}
