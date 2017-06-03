package nl.yoshuan.pricecomparer.ah.internal;

import com.google.gson.JsonArray;
import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class AhDataScraperJsonPathDemoUTest {

    private static final String JSON_FILE_LOCATION = "ah/ah-groente-tomaatkomkommer-tomaten-page.json";
    private AhDataScraperJsonPathDemo ahDataScraperJsonPathDemo;

    @Before
    public void initDataParser() {
        ahDataScraperJsonPathDemo = new AhDataScraperJsonPathDemo(JSON_FILE_LOCATION);
    }

    @Test(expected = NullPointerException.class)
    public void init_ThrowExceptionWhenFileNotFound() {
        ahDataScraperJsonPathDemo = new AhDataScraperJsonPathDemo("nonExistentFile");
    }

    @Test
    public void getJson_checkJsonFileReadData() {
        assertThat(ahDataScraperJsonPathDemo.getReadContext(), is(not("")));
    }

    @Test
    public void getJson_checkJsonPathWorking() {
        assertThat(ahDataScraperJsonPathDemo.getReadContext().read("$._links.self.href"), is(not("")));
    }

    @Test
    public void getProductNames_checkWorking() {
        JsonArray productDescriptions = ahDataScraperJsonPathDemo.getProductNames();

        assertThat(productDescriptions.get(0).toString(), is("\"AH TrostoÂ\u00ADmaÂ\u00ADten\""));
        assertThat(productDescriptions.size(), is(20));
    }

    @Test
    public void getSingleProduct_checkWorking() {
        List<Map<String, Object>> products = ahDataScraperJsonPathDemo.getProducts();
        Map<String, Object> product = products.get(0);

        assertThat(product.get("brandName"), is("AH"));
        assertThat(product.get("unitSize"), is("5 stuks"));
        assertThat(product.get("description"), is("AH TrostoÂ\u00ADmaÂ\u00ADten"));
    }

    @Test
    public void getAhProducts_checkWorking() {
        List<AhProduct> product = ahDataScraperJsonPathDemo.getAhProduct();

        assertThat(product.size(), is(20));
        assertThat(product.get(0).getProductSrc(), is("wi128875"));
    }

}
