package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.entities.Product;
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
    public List<Product> getAllProductNamesAndCategoryId() {
        em.createQuery("SELECT e.name, e.category FROM " + entityClass.getSimpleName() + " e ORDER BY e.", entityClass)
                .getResultList();

        return null;
    }

}
