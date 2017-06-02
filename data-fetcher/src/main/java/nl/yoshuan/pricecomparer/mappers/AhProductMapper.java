package nl.yoshuan.pricecomparer.mappers;

import nl.yoshuan.pricecomparer.datafetcher.AhProduct;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static nl.yoshuan.pricecomparer.entities.ProductVariables.Supermarket.AH;

public final class AhProductMapper {

    private AhProductMapper() {}

    public static AhProduct mapAhProductToDbProduct(AhProduct ahProduct) {
        Category ahCategory = new Category(ahProduct.getCategoryName(), null);

        if (ahProduct.getDiscount() == null) {
            ahProduct.setDiscount(new AhProduct.Discount(null));
        }
        if (ahProduct.getPriceLabel().getPriceWas() == null) {
            ahProduct.getPriceLabel().setPriceWas("0");
        }
        if (ahProduct.getPropertyIcons() == null) {
            ahProduct.setPropertyIcons(new ArrayList<>());
        }

        List<ProductVariables> ahProductVariables = new ArrayList<>();
        ahProductVariables.add(
                new ProductVariables(ahProduct.getProductSrc()
                        , ahProduct.getImageSrc()
                        , (int) (Double.parseDouble(ahProduct.getPriceLabel().getPriceNow()) * 100)
                        , (int) (Double.parseDouble(ahProduct.getPriceLabel().getPriceWas()) * 100)
                        , ahProduct.getDiscount().getLabel()
                        , ahProduct.getDiscountImageSrc()
                        , AH
                        , ahProduct.getPropertyIcons().toString()
                        , new Date()));

        Product product =
                new Product(ahProduct.getName()
                        , ahProduct.getUnitSize()
                        , ahProduct.getBrandName()
                        , ahCategory); // dont add the productVariable,
        // You will get a TransientPropertyValueException, because you are trying to
        // persist an object that has a reference to a transient object (productVariable)
        // meaning productVariable is not yet persisted.

        ahProductVariables.get(0).setProduct(product);

        ahProduct.setCategory(ahCategory);
        ahProduct.setProduct(product);
        ahProduct.setProductVariables(ahProductVariables.get(0));

        return ahProduct;
    }

}
