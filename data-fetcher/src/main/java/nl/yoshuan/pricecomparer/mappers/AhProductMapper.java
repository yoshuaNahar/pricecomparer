package nl.yoshuan.pricecomparer.mappers;

import nl.yoshuan.pricecomparer.datafetcher.AhProduct;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class AhProductMapper {

    private AhProductMapper() {}

    public static Product mapAhProductToDbProduct(AhProduct ahProduct) {
        Category ahCategory = new Category(ahProduct.getCategoryName(), null);

        List<ProductVariables> ahProductVariables = new ArrayList<>();
        ahProductVariables.add(
                new ProductVariables(ahProduct.getProductSrc()
                        , ahProduct.getImageSrc()
                        , Integer.parseInt(ahProduct.getPriceLabel().getPriceNow())
                        , Integer.parseInt(ahProduct.getPriceLabel().getPriceNow())
                        , ahProduct.getDiscount().getLabel()
                        , ahProduct.getDiscountImageSrc()
                        , "AH"
                        , ahProduct.getPropertyIcons().toString()
                        , new Date()));

        Product product =
                new Product(ahProduct.getName()
                        , ahProduct.getUnitSize()
                        , ahProduct.getBrandName()
                        , ahCategory
                        , ahProductVariables);

        ahProductVariables.get(0).setProduct(product);

        return product;
    }

}
