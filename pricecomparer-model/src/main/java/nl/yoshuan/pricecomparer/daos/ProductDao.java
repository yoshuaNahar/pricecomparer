package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.entities.Product;

import java.util.List;

public interface ProductDao extends GenericDao<Product,Long> {

    Product persistIfNotExist(Product product);

    List<Product> getAllProductNamesAndCategoryId();

}
