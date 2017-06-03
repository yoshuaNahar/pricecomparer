package nl.yoshuan.pricecomparer;

import nl.yoshuan.pricecomparer.ah.dbhandler.AhDbHandler;
import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.ah.scraper.AhDataScraper;
import nl.yoshuan.pricecomparer.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class App {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        AhDbHandler dbHandler = ctx.getBean(AhDbHandler.class);
        AhDataScraper ahDataScraper = ctx.getBean(AhDataScraper.class);

        List<AhProduct> ahProducts = ahDataScraper.getAllAhProductsFrom("/producten/aardappel-groente-fruit/groente");

        // If product already exists don't persist, same for categories. ProductVariables will persist each time!
        dbHandler.persistEntitiesToDb(ahProducts);
    }

}
