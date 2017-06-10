package nl.yoshuan.pricecomparer;

import nl.yoshuan.pricecomparer.ah.dbhandler.AhDbHandler;
import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.ah.scraper.AhDataScraper;
import nl.yoshuan.pricecomparer.config.AppConfig;
import nl.yoshuan.pricecomparer.jumbo.dbhandler.JumboDbHandler;
import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import nl.yoshuan.pricecomparer.jumbo.scraper.JumboDataScraper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

// ultimately I will use my server from school and cronjob this module once a week
public class App {

    public static long SLEEP_TIME_BETWEEN_REQUESTS = 500;
    private static ApplicationContext ctx;

    public static void main(String[] args) {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        getAhData();
        getJumboData();
    }

    private static void getAhData() {
        AhDataScraper ahDataScraper = ctx.getBean(AhDataScraper.class);
        AhDbHandler ahDbHandler = ctx.getBean(AhDbHandler.class);

        List<AhProduct> ahProducts = ahDataScraper.getAllProducts();
        // If product already exists don't persist, same for categories. ProductVariables will persist each time!
        ahDbHandler.persistEntitiesToDb(ahProducts);
    }

    private static void getJumboData() {
        JumboDataScraper jumboDataScraper = ctx.getBean(JumboDataScraper.class);
        JumboDbHandler jumboDbHandler = ctx.getBean(JumboDbHandler.class);

        List<JumboProduct> jumboProducts = jumboDataScraper.getAllProducts();
        jumboDbHandler.persistEntitiesToDb(jumboProducts, true);
//        jumboDbHandler.persistEntitiesToDb(jumboProducts);
    }

}
