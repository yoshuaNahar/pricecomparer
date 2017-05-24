package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Product;

public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao {

    public ProductDaoImpl() {
        super(Product.class);
    }

    // Gonna implement this in the future like inside CategoryDaoImpl
    @Override
    public Product persistIfNotExist(Product entity) {
        return null;
    }

}
