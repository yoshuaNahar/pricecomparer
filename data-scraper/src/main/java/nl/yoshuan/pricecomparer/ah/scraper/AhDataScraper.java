package nl.yoshuan.pricecomparer.ah.scraper;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.TypeRef;
import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.ah.util.JsonPathConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static nl.yoshuan.pricecomparer.ah.util.UrlDataReader.readJsonFrom;

@Component
public class AhDataScraper {

    private static final Logger logger = LoggerFactory.getLogger(AhDataScraper.class);

    static {
        JsonPathConfig.setJsonPathProvider();
    }

    // This will have to be recursive. The basis is when there is no
    // Filters with label Soort in the json page (meaning it is the deepest subCategory)
    public List<AhProduct> getAllProductsFrom(String categoryName) {
        ReadContext readContext = JsonPath.using(Configuration.defaultConfiguration())
                .parse(readJsonFrom("https://www.ah.nl/service/rest" + categoryName));

        TypeRef<List<String>> typeRef = new TypeRef<List<String>>() {};

        List<AhProduct> ahProducts = new ArrayList<>();

        // Iterate through the last FilterItems to get the individual subcategories
        List<String> subCategories = readContext.read("$._embedded.lanes[?(@.id == 'Filters')]._embedded.items[0]._embedded.filters[?(@.label == 'Soort')]._embedded.filterItems[*].navItem.link.href", typeRef);
        logger.debug(subCategories.toString());

        // if no subcategories
        if (subCategories.size() == 0) {
            ahProducts = getAhProductsFrom(readContext);
            ahProducts.forEach(ahProduct -> ahProduct.setFullCategoryName(categoryName));
            logger.debug(ahProducts.toString());
        } else {
            // subCategories exist, so enter first
            for (String subCategory : subCategories) {
                logger.debug("Current subcategory: " + subCategory);
                ahProducts.addAll(getAllProductsFrom(subCategory));
            }
        }

        return ahProducts;
    }

    private List<AhProduct> getAhProductsFrom(ReadContext readContext) {
        TypeRef<List<AhProduct>> typeRef = new TypeRef<List<AhProduct>>() {};

        List<AhProduct> product = readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product", typeRef);
        setImgSrcs(product, readContext);

        return product;
    }

    private void setImgSrcs(List<AhProduct> products, ReadContext readContext) {
        TypeRef<List<String>> typeRef = new TypeRef<List<String>>() {};

        List<String> imgSrcs = readContext.read("$._embedded.lanes[*]._embedded.items[*]._embedded.product.images[2].link.href", typeRef);

        for (int i = 0; i < products.size(); i++) {
            products.get(i).setImageSrc(imgSrcs.get(i));
        }
    }

}
