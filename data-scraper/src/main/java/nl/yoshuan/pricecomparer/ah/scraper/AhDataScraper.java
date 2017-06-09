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

import static nl.yoshuan.pricecomparer.App.SLEEP_TIME_BETWEEN_REQUESTS;
import static nl.yoshuan.pricecomparer.ah.util.UrlDataReader.readJsonFrom;

@Component
public class AhDataScraper {

    private static final Logger logger = LoggerFactory.getLogger(AhDataScraper.class);

    static {
        JsonPathConfig.setJsonPathProvider();
    }

    // This will have to be recursive. The basis is when there is no
    // Filters with label Soort in the json page (meaning it is the deepest subCategory)
    public List<AhProduct> getAllAhProductsFrom(String categoryName) {
        ReadContext readContext = JsonPath.using(Configuration.defaultConfiguration())
                .parse(readJsonFrom("https://www.ah.nl/service/rest" + categoryName));

        List<AhProduct> ahProducts = new ArrayList<>();
        // Iterate through the last FilterItems to get the individual subcategories
        List<String> subCategories = readContext
                .read("$._embedded.lanes[?(@.id == 'Filters')]._embedded.items[0]._embedded.filters[?(@.label == 'Soort')]._embedded.filterItems[*].navItem.link.href", new TypeRef<List<String>>() {});
        logger.debug(subCategories.toString());

        // if no subcategories
        if (subCategories.size() == 0) {
            ahProducts = getAhProductsFrom(readContext);
            ahProducts.forEach(ahProduct -> ahProduct.setFullCategoryName(categoryName));
            logger.debug(ahProducts.toString());
        } else {
            // subCategories exist, so enter first
            for (String subCategory : subCategories) {
                waitBeforeSendingRequest();
                logger.debug("Current subcategory: " + subCategory);
                ahProducts.addAll(getAllAhProductsFrom(subCategory));
            }
        }

        logger.debug("ahProducts size: " + ahProducts.size());
        return ahProducts;
    }

    private List<AhProduct> getAhProductsFrom(ReadContext readContext) {
        List<AhProduct> products = readContext
                .read("$._embedded.lanes[*]._embedded.items[*]._embedded.product", new TypeRef<List<AhProduct>>() {});
        System.out.println("sdasddas" + products.size());
        System.out.println(products.toString());
        // get all elements with an id inside the last _embedded element
        setImgSrcs(products, readContext);

        return products;
    }

    private void setImgSrcs(List<AhProduct> products, ReadContext readContext) {
        for (int i = 0; i < products.size(); i++) {
            // I am doing this for each product instead of getting all hrefs in 1 search, because some products from ah dont have images
            // Now I'm getting the img src for each product with the product id
            List<String> imgSrcs = readContext
                    .read("$._embedded.lanes[?(@.type == 'ProductLane')]._embedded.items[?(@.resourceType == 'Product')]._embedded.product[?(@.id == '" + products.get(i).getProductSrc() + "')].images[2].link.href", new TypeRef<List<String>>() {});
            if (imgSrcs.isEmpty()) {
                return;
            }
            String imgSrc = imgSrcs.get(0);

            System.out.println(products.get(i).getProductSrc());
            products.get(i).setImageSrc(imgSrc);
        }
    }

    private void waitBeforeSendingRequest() {
        try {
            Thread.sleep(SLEEP_TIME_BETWEEN_REQUESTS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
