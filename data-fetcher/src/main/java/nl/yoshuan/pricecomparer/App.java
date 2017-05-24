package nl.yoshuan.pricecomparer;

public class App {

    public static void main(String[] args) {
        AhDataFetcher ahDataFetcher = new AhDataFetcher();

        List<AhProduct> ahProducts =  ahDataFetcher.goToFirstInnerMostPage("/producten/aardappel-groente-fruit/groente/tomaat-komkommer/tomaten/trostomaat");

        DbHandler dbHandler = new DbHandler(new CategoryDaoImpl(), new ProductDaoImpl(), new ProductVariablesDaoImpl());
        dbHandler.addProducts(ahProducts);
    }

}
