package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Product;

import java.util.List;

public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao {

    public ProductDaoImpl() {
        super(Product.class);
    }

    // Gonna implement this in the future like inside CategoryDaoImpl
    @Override
    public Product persistIfNotExist(Product product) {
        List<Product> products = findByPropertyValue("name", product.getName());
        Product managedProduct = null;
        if (products.size() > 0) {
            managedProduct = products.get(0);
        }
        if (managedProduct == null) {
            em.persist(product);
            return product;
        }
        return managedProduct;
    }

}
