package nl.yoshuan.pricecomparer.datafetcher;

import com.google.gson.JsonArray;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AhDataFetcher {

    private static final Logger logger = LoggerFactory.getLogger(AhDataFetcher.class);

    public AhDataFetcher() {
        setConfiguration();
    }

    // This will have to be recursive. The basis is when there is no
    // Filters with label Soort in the json page (meaning it is the deepest link)
    public JsonArray goToCategoryPage(String category) {
        ReadContext readContext = JsonPath.using(Configuration.defaultConfiguration())
                .parse(readJsonFromUrl("https://www.ah.nl/service/rest/producten/" + category));

        return readContext
                .read("$._embedded.lanes[*]._embedded.items[*]._embedded.product.description");
    }

    public List<AhProduct> goToFirstInnerMostPage(String category) {
        ReadContext readContext = JsonPath.using(Configuration.defaultConfiguration())
                .parse(readJsonFromUrl("https://www.ah.nl/service/rest" + category));

        TypeRef<List<String>> typeRef = new TypeRef<List<String>>() {};

        // Iterate through the last FilterItems to get the individual subcategories
        List<String> subCategories = readContext.read("$._embedded.lanes[?(@.id == 'Filters')]._embedded.items[0]._embedded.filters[?(@.label == 'Soort')]._embedded.filterItems[*].navItem.link.href", typeRef);

        logger.info(subCategories.toString());

        Iterator<String> subCategoriesIterator = subCategories.iterator();
        while (subCategoriesIterator.hasNext()) {
            String subCategory = subCategoriesIterator.next();

            logger.info("Current subcategory: " + subCategory);

            goToFirstInnerMostPage(subCategory);
        }

        if (subCategories.size() == 0) {
            AhDataParser dataParser = new AhDataParser(readContext);
            List<AhProduct> ahProducts = dataParser.getAhProduct();
            ahProducts.forEach(ahProduct -> ahProduct.setFullCategoryName(category));
            if (logger.isDebugEnabled()) { // Im still in development
                logger.debug(ahProducts.toString());
            }
            return ahProducts;
        }

        return null;
    }

    public String readJsonFromUrl(String url) {
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            return readAll(rd);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

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
