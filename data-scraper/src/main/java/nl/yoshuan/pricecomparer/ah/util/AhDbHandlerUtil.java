package nl.yoshuan.pricecomparer.ah.util;

import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import nl.yoshuan.pricecomparer.util.DbEntitiesHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static nl.yoshuan.pricecomparer.entities.ProductVariables.Supermarket.AH;

// naming packages: https://softwareengineering.stackexchange.com/questions/75919/should-package-names-be-singular-or-plural
public class AhDbHandlerUtil {

    private AhDbHandlerUtil() {}

    public static DbEntitiesHolder mapToDbEntities(AhProduct ahProduct) {
        // not gonna use ahProduct.getCategoryName()
        Category category = new Category(ahProduct.getFullCategoryName(), null);

        if (ahProduct.getDiscount() == null) {
            ahProduct.setDiscount(new AhProduct.Discount(null));
        }
        if (ahProduct.getPriceLabel().getPriceWas() == null) {
            ahProduct.getPriceLabel().setPriceWas("0");
        }
        if (ahProduct.getPropertyIcons() == null) {
            ahProduct.setPropertyIcons(new ArrayList<>());
        }

        ProductVariables productVariables =
                new ProductVariables(ahProduct.getProductSrc()
                        , ahProduct.getImageSrc()
                        , (int) (Double.parseDouble(ahProduct.getPriceLabel().getPriceNow()) * 100)
                        , (int) (Double.parseDouble(ahProduct.getPriceLabel().getPriceWas()) * 100)
                        , ahProduct.getDiscount().getLabel()
                        , ahProduct.getDiscountImageSrc()
                        , AH
                        , ahProduct.getPropertyIcons().toString()
                        , new Date()
                        , null);

        Product product =
                new Product(ahProduct.getName()
                        , ahProduct.getUnitSize()
                        , ahProduct.getBrandName()
                        , category
                        , null); // dont add the productVariable,
        // You will get a TransientPropertyValueException, because you are trying to
        // persist an object that has a reference to a transient object (productVariable)

        // Some AH products like, https://www.ah.nl/producten/product/wi233066/tasty-tom-gold has no brand name!
        if (product.getBrand() == null) {
            product.setBrand("AH");
        }

        return new DbEntitiesHolder(category, product, productVariables);
    }

    public static List<Category> splitFullCategory(String fullCategoryName) {
        // the full category will usually be of this type, so I remove the first producten substring
        // /producten/aardappel-groente-fruit/groente/tomaat-komkommer/tomaten/trostomaten
        String fullCategoryNameExceptProducten = fullCategoryName.replace("/producten/", "");

        String[] categoryNames = fullCategoryNameExceptProducten.split(Pattern.quote("/")); // split on /

        List<Category> linkedCategories = new ArrayList<>();

        for (String categoryName : categoryNames) {
            linkedCategories.add(new Category(categoryName, null));
        }

        for (int i = 1; i < linkedCategories.size(); i++) { // add parent to child, so that db parent_id column is not null
            linkedCategories.get(i).setParentCategory(linkedCategories.get(i - 1));
        }

        return linkedCategories;
    }

}
