package nl.yoshuan.pricecomparer.dataparser.jumbo;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class JumboDataParserUTest {

    private static final String HTML_FILE_LOCATION = "./jumbo-sample-data.html";
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

        assertThat(products.get(0).getId(), is("Jumbo Vrij Vastkokende Aardappelen 3kg"));
        assertThat(products.size(), is(9));
    }

    @Test
    public void test() {
        jumboDataParser.test();
    }

}
