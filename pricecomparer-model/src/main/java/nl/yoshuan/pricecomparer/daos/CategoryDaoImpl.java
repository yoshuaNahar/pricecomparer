package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.entities.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDaoImpl extends GenericDaoImpl<Category, Long> implements CategoryDao {

    public CategoryDaoImpl() {
        super(Category.class);
    }

    // When using this method, set category = persistIfNotExist(..), because in contrary to persist, this method doesn't set the ID automatically
    @Override
    public Category persistIfNotExist(Category category) {
        Category managedCategory = findByUniquePropertyValue("name", category.getName());
        if (managedCategory == null) {
            return persist(category);
        }
        return managedCategory;
    }
    // I could've also placed this inside the genericClass but then I would've needed to use reflection for the field name, and use abstract
    // method to get the Entity.getName

}
