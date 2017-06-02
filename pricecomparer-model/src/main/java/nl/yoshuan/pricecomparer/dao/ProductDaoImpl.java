package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao {

    public ProductDaoImpl() {
        super(Product.class);
    }

    @Override
    public Product persistIfNotExist(Product product) {
        Product managedProduct = findByUniquePropertyValue("name", product.getName());
        if (managedProduct == null) {
            return persist(product);
        } else {
            return em.getReference(Product.class, managedProduct.getId());
        }
    }

}
