package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.SimpleProduct;

import java.util.List;

public interface ProductDao extends GenericDao<Product, Long> {

    Product persistIfNotExist(Product product);

    List<SimpleProduct> getAllProductNamesAndCategoryId();

    List<Product> findByCategoryId(Long categoryId, int startAt, int maxResult);

}
