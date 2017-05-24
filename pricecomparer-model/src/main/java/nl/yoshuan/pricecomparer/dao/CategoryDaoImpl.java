package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Category;

public class CategoryDaoImpl extends GenericDaoImpl<Category, Long> implements CategoryDao {

    public CategoryDaoImpl() {
        super(Category.class);
    }

    @Override
    public Category persistIfNotExist(Category category) {
        if (!existsByPropertyValue("categoryName", category.getCategoryName())) {
            em.persist(category);
        }
        return category;
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
