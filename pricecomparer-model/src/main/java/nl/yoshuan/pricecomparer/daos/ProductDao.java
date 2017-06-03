package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.entities.Product;

public interface ProductDao extends GenericDao<Product,Long> {

    Product persistIfNotExist(Product product);

}
