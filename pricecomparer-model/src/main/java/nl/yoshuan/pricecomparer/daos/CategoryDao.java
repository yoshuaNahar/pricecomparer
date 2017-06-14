package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.entities.Category;

public interface CategoryDao extends GenericDao<Category, Long> {

    Category persistIfNotExist(Category category);

    /**
     * This method is needed because I only want to load the first childCategories list.
     * <br>
     * When I convert to json, each category inside the childCategories will initiate a
     * lazy load on his childCategories.
     */
    Category unproxyOnlyGetFirstChildCategory(Category category);

}
