package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Category;

import java.util.List;

public class CategoryDaoImpl extends GenericDaoImpl<Category, Long> implements CategoryDao {

    public CategoryDaoImpl() {
        super(Category.class);
    }

    // I couldve used existsByPropertyValue, but if I use that I still need
    // to add the findBypropertyValue inside the else part
    @Override
    public Category persistIfNotExist(Category category) {
        List<Category> categories = findByPropertyValue("categoryName", category.getCategoryName());
        Category managedCategory = null;
        if (categories.size() > 0) {
            managedCategory = categories.get(0);
        }
        if (managedCategory == null) {
            em.persist(category);
            return category;
        }
        return managedCategory;
    }

// I can't use this, because If an exception is thrown I their is no transaction to commit, which causes an IllegalStateException: Transaction not active to be thrown.
//    @Override
//    public Category persistIfNotExist(Category category) {
//        try {
//            super.persist(category);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return category;
//    }


}
