package nl.yoshuan.pricecomparer.services;

import nl.yoshuan.pricecomparer.daos.ProductDao;
import nl.yoshuan.pricecomparer.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product getProductById(Long productId) {
        return productDao.findById(productId);
    }

    public Product getProductByName(String productName) {
        return productDao.findByUniquePropertyValue("name", productName);
    }

}
