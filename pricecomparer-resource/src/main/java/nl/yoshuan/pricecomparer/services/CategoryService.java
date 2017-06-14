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
        Category category = categoryDao.findById(categoryId);

        return categoryDao.unproxyOnlyGetFirstChildCategory(category);
    }

    public Category getCategoryByName(String categoryName) {
        Category category = categoryDao.findByUniquePropertyValue("name", categoryName);

        return categoryDao.unproxyOnlyGetFirstChildCategory(category);
    }

    public List<Product> getProductsByCategoryId(Long categoryId, int pageNumber) {
        Category category = categoryDao.findById(categoryId);

        return getProductsByCategory(category, pageNumber);
    }

    public List<Product> getProductsByCategoryName(String categoryName, int pageNumber) {
        Category category = categoryDao.findByUniquePropertyValue("name", categoryName);

        return getProductsByCategory(category, pageNumber);
    }

    private List<Product> getProductsByCategory(Category category, int pageNumber) {
        List<Category> deepestCategories = getDeepestChildCategory(category);
        if (deepestCategories.size() == 0) {
            deepestCategories.add(category);
        }

        List<Product> products = new ArrayList<>();
        deepestCategories.stream().limit(12).forEach(deepestCategory ->
                products.addAll(productDao.findByCategoryId(deepestCategory.getId(), pageNumber
                        * 12, maxResult(deepestCategories.size()))));

        return products;
    }

    private List<Category> getDeepestChildCategory(Category category) {
        List<Category> childCategories = category.getChildCategories();
        List<Category> deepestChildCategories = new ArrayList<>();

        for (Category childCategory : childCategories) {
            if (isDeepestSubCategory(childCategory)) {
                deepestChildCategories.add(childCategory);
            } else {
                deepestChildCategories.addAll(getDeepestChildCategory(childCategory));
            }

            if (deepestChildCategories.size() > 12) {
                break;
            }
        }

        return deepestChildCategories;
    }

    private boolean isDeepestSubCategory(Category category) {
        return category.getChildCategories().size() == 0;
    }

    private int maxResult(int categorySize) {
        return (int) Math.ceil(((double) 12 / categorySize));
    }

}
