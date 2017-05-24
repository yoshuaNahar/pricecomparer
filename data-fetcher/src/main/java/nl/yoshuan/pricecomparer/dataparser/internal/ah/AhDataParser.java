package nl.yoshuan.pricecomparer.dataparser.internal.ah;

import com.google.gson.JsonArray;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import nl.yoshuan.pricecomparer.datafetcher.AhProduct;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public final class AhDataParser {

    private final String jsonFileLocation;
    private final ReadContext readContext;

    public AhDataParser(String jsonFileLocation) {
        this.jsonFileLocation = jsonFileLocation;
        setConfiguration();
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
        TypeRef<List<String>> typeRef = new TypeRef<List<String>>() {
        };

        List<String> imgSrcs = readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product.images[2].link.href", typeRef);

        int productsLength = products.size();
        for (int i = 0; i < productsLength; i++) {
            products.get(i).setImageSrc(imgSrcs.get(i));
        }
    }

    // Sets configuration for JsonPath to use GsonJsonProvider.
    // Defaults is a static field inside Configuration class, that is why I use it this way.
    private void setConfiguration() {
        Configuration.setDefaults(new Configuration.Defaults() {
            private final JsonProvider jsonProvider = new GsonJsonProvider();
            private final MappingProvider mappingProvider = new GsonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

}
