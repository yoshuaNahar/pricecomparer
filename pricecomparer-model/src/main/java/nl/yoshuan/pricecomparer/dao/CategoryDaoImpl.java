package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDaoImpl extends GenericDaoImpl<Category, Long> implements CategoryDao {

    public CategoryDaoImpl() {
        super(Category.class);
    }

    // I could've used existsByUniquePropertyValue, but if I use that I still need
    // to add the findBypropertyValue inside the else part

    // When using this set the category = persistIfNotExist(..), in contrary to persist, this method doesnt set the ID automatically
    @Override
    public Category persistIfNotExist(Category category) {
        Category managedCategory = findByUniquePropertyValue("name", category.getName());
        if (managedCategory == null) {
            return persist(category);
        } else {
            return em.getReference(Category.class, managedCategory.getId());
        }
    }
    // I could've also placed this inside the genericClass but then I would've needed to use reflection for the field name, and use 2 abstract
    // methods to get the Entity.getName and entity.getId

}
