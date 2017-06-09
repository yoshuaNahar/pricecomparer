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

import java.util.ArrayList;
import java.util.List;

// ultimately I will use my server from school and cronjob this module once a week
public class App {

    private static final List<String> MAIN_AH_CATEGORIES = new ArrayList<>();
    public static long SLEEP_TIME_BETWEEN_REQUESTS = 500;
    private static ApplicationContext ctx;

    static {
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

    public static void main(String[] args) {
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        getAhData();
    }

    private static void getAhData() {
        AhDataScraper ahDataScraper = ctx.getBean(AhDataScraper.class);
        AhDbHandler dbHandler = ctx.getBean(AhDbHandler.class);

        // I already did 0 en 1
        for (int i = 0; i < 1; i++) {
            List<AhProduct> ahProducts = ahDataScraper.getAllAhProductsFrom("/producten/huishouden-huisdier"); // /producten/koken-tafelen-non-food
            // If product already exists don't persist, same for categories. ProductVariables will persist each time!
            dbHandler.persistEntitiesToDb(ahProducts);
            System.out.println(MAIN_AH_CATEGORIES.get(i));
        }
    }

    private static void getJumboData() {
        JumboDataScraper jumboDataScraper = ctx.getBean(JumboDataScraper.class);
        JumboDbHandler jumboDbHandler = ctx.getBean(JumboDbHandler.class);

        List<JumboProduct> jumboProducts = jumboDataScraper.scrapePages(1);
        jumboDbHandler.persistEntitiesToDb(jumboProducts);
    }

}
