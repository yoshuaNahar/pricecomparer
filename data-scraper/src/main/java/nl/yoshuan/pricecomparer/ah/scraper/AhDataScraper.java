package nl.yoshuan.pricecomparer.ah.scraper;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.TypeRef;
import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.ah.util.JsonPathConfig;
import nl.yoshuan.pricecomparer.shared.DataScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static nl.yoshuan.pricecomparer.App.SLEEP_TIME_BETWEEN_REQUESTS;
import static nl.yoshuan.pricecomparer.ah.util.UrlDataReader.readJsonFrom;

@Component
public class AhDataScraper extends DataScraper<AhProduct> {

    public static final List<String> MAIN_AH_CATEGORIES = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(AhDataScraper.class);

    static {
        JsonPathConfig.setJsonPathProvider();

        // I am adding /producten, because when I get the subcategories, I also get
        // /producten/aardappel-groente-fruit/groente for example. So in my getAhProductsFrom()
        // method I can just past that in the url
        MAIN_AH_CATEGORIES.add("/producten/aardappel-groente-fruit");
        MAIN_AH_CATEGORIES.add("/producten/verse-kant-en-klaar-maaltijden-salades");
        MAIN_AH_CATEGORIES.add("/producten/vlees-kip-vis-vega");
        MAIN_AH_CATEGORIES.add("/producten/kaas-vleeswaren-delicatessen");
        MAIN_AH_CATEGORIES.add("/producten/zuivel-eieren");
        MAIN_AH_CATEGORIES.add("/producten/bakkerij");
        MAIN_AH_CATEGORIES.add("/producten/ontbijtgranen-broodbeleg-tussendoor");
        MAIN_AH_CATEGORIES.add("/producten/frisdrank-sappen-koffie-thee");
        MAIN_AH_CATEGORIES.add("/producten/wijn");
        MAIN_AH_CATEGORIES.add("/producten/bier-sterke-drank-aperitieven");
        MAIN_AH_CATEGORIES.add("/producten/pasta-rijst-internationale-keuken");
        MAIN_AH_CATEGORIES.add("/producten/soepen-conserven-sauzen-smaakmakers");
        MAIN_AH_CATEGORIES.add("/producten/snoep-koek-chips");
        MAIN_AH_CATEGORIES.add("/producten/diepvries");
        MAIN_AH_CATEGORIES.add("/producten/drogisterij-baby");
        MAIN_AH_CATEGORIES.add("/producten/bewuste-voeding");
        MAIN_AH_CATEGORIES.add("/producten/huishouden-huisdier");
        MAIN_AH_CATEGORIES.add("/producten/koken-tafelen-non-food");
    }

    @Override
    public List<AhProduct> getAllProducts() {
        List<AhProduct> ahProducts = new ArrayList<>();

        for (String mainAhCategory : MAIN_AH_CATEGORIES) {
            ahProducts.addAll(getAhProductsFrom(mainAhCategory));
        }

        return ahProducts;
    }

    // This will have to be recursive. The basis is when there is no
    // Filters with label Soort in the json page (meaning it is the deepest subCategory)

    /**
     * Ah products are split into 16 main categories. This method scrapes all
     * products from a chosen category.
     *
     * @param categoryName one of the sixteen main categories inside <code>MAIN_AH_CATEGORIES</code>
     * @return list of scraped <code>AhProduct</code>s
     */
    public List<AhProduct> getAhProductsFrom(String categoryName) {
        ReadContext readContext = JsonPath.using(Configuration.defaultConfiguration())
                .parse(readJsonFrom("https://www.ah.nl/service/rest" + categoryName));

        List<AhProduct> ahProducts = new ArrayList<>();
        // Iterate through the last FilterItems to get the individual subcategories
        List<String> subCategories = readContext
                .read("$._embedded.lanes[?(@.id == 'Filters')]._embedded.items[0]._embedded.filters[?(@.label == 'Soort')]._embedded.filterItems[*].navItem.link.href", new TypeRef<List<String>>() {});
        logger.info(subCategories.toString());

        // if no subcategories
        if (subCategories.size() == 0) {
            ahProducts = getAhProductsFrom(readContext);
            ahProducts.forEach(ahProduct -> ahProduct.setFullCategoryName(categoryName));
            logger.info(ahProducts.toString());
        } else {
            // subCategories exist, so enter all subCategories
            for (String subCategory : subCategories) {
                waitBetweenRequests(SLEEP_TIME_BETWEEN_REQUESTS);
                logger.info("Current subcategory: " + subCategory);
                ahProducts.addAll(getAhProductsFrom(subCategory));
            }
        }

        logger.info("Total ahProducts size: " + ahProducts.size());
        return ahProducts;
    }

    private List<AhProduct> getAhProductsFrom(ReadContext readContext) {
        List<AhProduct> products = readContext
                .read("$._embedded.lanes[*]._embedded.items[*]._embedded.product", new TypeRef<List<AhProduct>>() {});
        logger.info("ahProducts size: " + products.size());
        logger.info(products.toString());

        // because I get this data for the names for some reason AH BASIC Pi­ta
        products.forEach(ahProduct ->
                ahProduct.setName(ahProduct.getName().replace("\u00AD", "")));

        // get all elements with an id inside the last _embedded element
        setImgSrcs(products, readContext);

        return products;
    }

    private void setImgSrcs(List<AhProduct> products, ReadContext readContext) {
        for (AhProduct product : products) {
            // I am doing this for each product instead of getting all hrefs in 1 search, because some products from ah dont have images
            // Now I'm getting the img src for each product with the product id
            List<String> imgSrcs = readContext
                    .read("$._embedded.lanes[?(@.type == 'ProductLane')]._embedded.items[?(@.resourceType == 'Product')]._embedded.product[?(@.id == '" + product.getProductSrc() + "')].images[2].link.href", new TypeRef<List<String>>() {});

            if (imgSrcs.isEmpty()) {
                return;
            }

            product.setImageSrc(imgSrcs.get(0));
        }
    }

}
