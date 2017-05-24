package nl.yoshuan.pricecomparer.dataparser.internal.ah;

import com.google.gson.JsonArray;
import nl.yoshuan.pricecomparer.datafetcher.AhProduct;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class AhDataParserUTest {
    
    private static final String JSON_FILE_LOCATION = "ah/ah-groente-tomaatkomkommer-tomaten-page.json";
    private AhDataParser ahDataParser;
    
    @Before
    public void initDataParser() {
        ahDataParser = new AhDataParser(JSON_FILE_LOCATION);
    }
    
    @Test(expected = NullPointerException.class)
    public void init_ThrowExceptionWhenFileNotFound() {
        ahDataParser = new AhDataParser("nonExistentFile");
    }
    
    @Test
    public void getJson_checkJsonFileReadData() {
        assertThat(ahDataParser.getReadContext(), is(not("")));
    }
    
    @Test
    public void getJson_checkJsonPathWorking() {
        assertThat(ahDataParser.getReadContext().read("$._links.self.href"), is(not("")));
    }
    
    @Test
    public void getProductNames_checkWorking() {
        JsonArray productDescriptions = ahDataParser.getProductNames();
        
        assertThat(productDescriptions.get(0).toString(), is("\"AH TrostoÂ\u00ADmaÂ\u00ADten\""));
        assertThat(productDescriptions.size(), is(20));
    }
    
    @Test
    public void getSingleProduct_checkWorking() {
        List<Map<String, Object>> products = ahDataParser.getProducts();
        Map<String, Object> product = products.get(0);
        
        assertThat(product.get("brandName"), is("AH"));
        assertThat(product.get("unitSize"), is("5 stuks"));
        assertThat(product.get("description"), is("AH TrostoÂ\u00ADmaÂ\u00ADten"));
    }
    
    @Test
    public void getAhProducts_checkWorking() {
        List<AhProduct> product = ahDataParser.getAhProduct();
        
        assertThat(product.size(), is(20));
        assertThat(product.get(0).getProductSrc(), is("wi128875"));
    }
    
}
