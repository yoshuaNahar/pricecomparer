package nl.yoshuan.pricecomparer;

import nl.yoshuan.pricecomparer.config.DbConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
//        AhDataFetcher ahDataFetcher = new AhDataFetcher();
//
//        List<AhProduct> ahProducts =  ahDataFetcher.goToFirstInnerMostPage("/producten/aardappel-groente-fruit/groente/tomaat-komkommer/tomaten");
////        List<AhProduct> ahProducts =  ahDataFetcher.goToFirstInnerMostPage("/producten/aardappel-groente-fruit/groente/tomaat-komkommer/tomaten/cherry-tomaten");
//
//
//        DbHandler dbHandler = new DbHandler(new CategoryDaoImpl(), new ProductDaoImpl(), new ProductVariablesDaoImpl());
//        dbHandler.addProducts(ahProducts);

        new App().testingDependencyInjection();
    }

    private void testingDependencyInjection() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(DbConfig.class); // This DbConfigTest class is from the model module.
        TestPojo pojo = ctx.getBean(TestPojo.class);
        System.out.println(pojo);

        TestPojo pojo2 = ctx.getBean(TestPojo.class);
        System.out.println(pojo2);

        pojo2.setName("OtherJason");

        TestPojo pojo3 = ctx.getBean(TestPojo.class);
        System.out.println(pojo3);

        System.out.println(pojo);
    }

}
