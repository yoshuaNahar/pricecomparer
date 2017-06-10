package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.SimpleProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        }

        return managedProduct;
    }

    @Override
    public List<SimpleProduct> getAllProductNamesAndCategoryId() {
        return em
                .createQuery("SELECT NEW nl.yoshuan.pricecomparer.entities.SimpleProduct(p.name, c.id) FROM Product p, Category c WHERE c.id = p.category.id", SimpleProduct.class)
                .getResultList();
    }

}
