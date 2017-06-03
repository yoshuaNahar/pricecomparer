package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.entities.Category;

public interface CategoryDao extends GenericDao<Category, Long> {

    Category persistIfNotExist(Category category);

}
