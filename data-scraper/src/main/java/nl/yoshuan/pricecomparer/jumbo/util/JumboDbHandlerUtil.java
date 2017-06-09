package nl.yoshuan.pricecomparer.jumbo.util;

import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import nl.yoshuan.pricecomparer.util.DbEntitiesHolder;

import java.util.Date;

import static nl.yoshuan.pricecomparer.entities.ProductVariables.Supermarket.JUMBO;

public class JumboDbHandlerUtil {

    private JumboDbHandlerUtil() {}

    public static DbEntitiesHolder mapToDbEntities(JumboProduct jumboProduct) {
        ProductVariables productVariables =
                new ProductVariables(jumboProduct.getProductSrc()
                        , jumboProduct.getImgUrl()
                        , jumboProduct.getPrice()
                        , jumboProduct.getBonusPrice()
                        , jumboProduct.getBonusType()
                        , jumboProduct.getBonusImg()
                        , JUMBO
                        , null
                        , new Date()
                        , null);

        Product product =
                new Product(jumboProduct.getName()
                        , jumboProduct.getUnitSize()
                        , jumboProduct.getBrandName()
                        , null // category will be inserted later
                        , null);

        // productVariables needs a product ref
        return new DbEntitiesHolder(null, product, productVariables);
    }

}
