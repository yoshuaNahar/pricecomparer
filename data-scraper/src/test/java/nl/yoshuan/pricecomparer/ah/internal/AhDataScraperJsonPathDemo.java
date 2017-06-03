package nl.yoshuan.pricecomparer.ah.internal;

import com.google.gson.JsonArray;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.TypeRef;
import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.ah.util.JsonPathConfig;
import org.junit.Ignore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Ignore("This is just a prototype to become familiar with using jsonPath")
public final class AhDataScraperJsonPathDemo {

    private final String jsonFileLocation;
    private final ReadContext readContext;

    public AhDataScraperJsonPathDemo(String jsonFileLocation) {
        this.jsonFileLocation = jsonFileLocation;
        JsonPathConfig.setJsonPathProvider();
        this.readContext = readLocalFile();
    }

    public ReadContext getReadContext() {
        return readContext;
    }

    public ReadContext readLocalFile() {
        File file = new File(getClass().getClassLoader().getResource(jsonFileLocation).getFile());
        StringBuilder result = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String resultClean = result.toString().replace("�", "");

        return JsonPath.using(Configuration.defaultConfiguration()).parse(resultClean);
    }

    public JsonArray getProductNames() {
        return readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product.description");
    }

    public List<Map<String, Object>> getProducts() {
        TypeRef<List<Map<String, Object>>> typeRef = new TypeRef<List<Map<String, Object>>>() {};

        return readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product", typeRef);
    }

    public List<AhProduct> getAhProduct() {
        TypeRef<List<AhProduct>> typeRef = new TypeRef<List<AhProduct>>() {};

        List<AhProduct> product = readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product", typeRef);
        setImgSrcs(product);

        return product;
    }

    private void setImgSrcs(List<AhProduct> products) {
        TypeRef<List<String>> typeRef = new TypeRef<List<String>>() {};

        List<String> imgSrcs = readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product.images[2].link.href", typeRef);

        for (int i = 0; i < products.size(); i++) {
            products.get(i).setImageSrc(imgSrcs.get(i));
        }
    }

}
