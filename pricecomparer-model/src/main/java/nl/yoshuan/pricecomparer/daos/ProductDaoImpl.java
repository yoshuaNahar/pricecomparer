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
    // JPQL projection. This method is used by Lucene
    public List<SimpleProduct> getAllProductNamesAndCategoryId() {
        return em
                .createQuery("SELECT NEW nl.yoshuan.pricecomparer.entities.SimpleProduct(p.name, c.id)"
                        + " FROM Product p, Category c"
                        + " WHERE c.id = p.category.id", SimpleProduct.class)
                .getResultList();
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId, int startAt) {
        return em
                .createQuery("SELECT p FROM Product p"
                        + " WHERE p.category.id = :categoryId", Product.class)
                .setParameter("categoryId", categoryId)
                .setFirstResult(startAt)
                .setMaxResults(12)
                .getResultList();
    }

    public List<Product> findByCategoryName(String categoryName, int startAt) {
        return em
                .createQuery("SELECT p FROM Product p"
                        + " JOIN p.category c"
                        + " WHERE c.name = :categoryName", Product.class)
                .setParameter("categoryName", categoryName)
                .setFirstResult(startAt)
                .setMaxResults(12)
                .getResultList();
    }

}
