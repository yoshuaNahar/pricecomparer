package nl.yoshuan.pricecomparer.services;

import nl.yoshuan.pricecomparer.daos.CategoryDao;
import nl.yoshuan.pricecomparer.daos.ProductDao;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    public Category getCategoryById(Long categoryId) {
        return categoryDao.findById(categoryId);
    }

    public Category getCategoryByName(String categoryName) {
        return categoryDao.findByUniquePropertyValue("name", categoryName);
    }

    public List<Product> getProductsByCategoryId(Long categoryId, int pageNumber) {
        return productDao.findByCategoryId(categoryId, pageNumber * 12);
    }

    public List<Product> getProductsByCategoryName(String categoryName, int pageNumber) {
        return productDao.findByCategoryName(categoryName, pageNumber * 12);
    }

    //    public Map<String, List<Product>> getProductsByCategoryId(Long categoryId, int pageNumber) {
//        Map<String, List<Product>> products = new HashMap<>();
//
//        Category category = categoryDao.findByUniquePropertyValue("name", categoryName);
//
//        if (isDeepestSubCategory(category)) {
//            products.put(categoryName, productDao.findByCategory(category, pageNumber));
//        } else {
//            List<Category> deepestChildCategory = getDeepestChildCategory(category);
//            deepestChildCategory.forEach(cat -> products.put(cat.getName(), productDao
//                    .findByCategory(cat, pageNumber)));
//        }
//
//        return products;
//    }

    private List<Category> getDeepestChildCategory(Category category) {
        List<Category> childCategories = category.getChildCategories();
        List<Category> deepestChildCategories = new ArrayList<>();

        for (Category childCategory : childCategories) {
            if (isDeepestSubCategory(childCategory)) {
                deepestChildCategories.add(childCategory);
                if (deepestChildCategories.size() == 12) {
                    return deepestChildCategories;
                }
            } else {
                deepestChildCategories.addAll(getDeepestChildCategory(category));
            }
        }

        return deepestChildCategories;
    }

    private boolean isDeepestSubCategory(Category category) {
        return category.getChildCategories().size() == 0;
    }

}
