package nl.yoshuan.pricecomparer.dbhandler;

import nl.yoshuan.pricecomparer.dao.CategoryDao;
import nl.yoshuan.pricecomparer.dao.ProductDao;
import nl.yoshuan.pricecomparer.dao.ProductVariablesDao;
import nl.yoshuan.pricecomparer.datafetcher.AhProduct;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.mappers.AhProductMapper;

import java.util.ArrayList;
import java.util.List;

public class DbHandler {

    private CategoryDao categoryDao;
    private ProductDao productDao;
    private ProductVariablesDao productVariablesDao;

    public DbHandler(CategoryDao categoryDao, ProductDao productDao, ProductVariablesDao productVariablesDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.productVariablesDao = productVariablesDao;
    }

    public void addProducts(List<AhProduct> ahProducts) {
        List<Product> dbProducts = new ArrayList<>();

        for (AhProduct ahProduct : ahProducts) {
            Product product = AhProductMapper.mapAhProductToDbProduct(ahProduct);
            dbProducts.add(product);
        }

        for (Product product : dbProducts) {
            categoryDao.persist(product.getCategory());
            productDao.persist(product);
            productVariablesDao.persist(product.getProductsVariables().get(0)); // First and Only
        }
    }

}
