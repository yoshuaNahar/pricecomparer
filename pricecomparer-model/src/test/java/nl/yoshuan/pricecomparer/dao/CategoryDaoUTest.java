package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.config.TestConfig;
import nl.yoshuan.pricecomparer.entities.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static nl.yoshuan.pricecomparer.utils.CategoryUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Link below for why I need this...
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-resetting-auto-increment-columns-before-each-test-method/
public class CategoryDaoUTest {
    // I didnt use TDD when developing this part. because these arent
    // even unit tests since I include the db.

    // I only added tests for the methods I will definitely use.
    // Only add more tests if I want to try out a specific feature or if I encountered a bug.

    @Autowired private CategoryDaoImpl categoryDao;

    @Test
    public void addCategoryAndFindIt() {
        Category category = createParentCategory();
        categoryDao.persist(category);

        Category managedCategory = categoryDao.findReferenceById(1L);

        assertThat(categoryDao.getCount(), is(1L));
        assertThat(managedCategory.getName(), is(GROENTE_FRUIT_AARDAPPELEN));
        assertThat(managedCategory.getParentCategory(), is(nullValue()));
    }

    @Test
    public void findCategoryIdNotFound() {
        assertThat(categoryDao.findById(999L), is(nullValue()));
    }

    @Test
    public void addTwoCategoriesAndFindBoth() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(parentCategory);

        categoryDao.persist(parentCategory);
        categoryDao.persist(childCategory);

        Category category = categoryDao.findReferenceById(2L);

        assertThat(categoryDao.getCount(), is(2L));
        assertThat(category.getName(), is(GROENTE));
        assertThat(category.getParentCategory().getName(), is(GROENTE_FRUIT_AARDAPPELEN));
        assertThat(category.getParentCategory().getParentCategory(), is(nullValue()));
    }

    @Test
    public void addTwoCategoriesPersistParentAndFindBoth() {
        Category parentCategory = createParentCategory();
        parentCategory.getChildCategories().add(new Category(GROENTE, parentCategory)); // CascadeType.ALL

        categoryDao.persist(parentCategory);

        Category category = categoryDao.findReferenceById(1L);

        assertThat(categoryDao.getCount(), is(2L));
        assertThat(category.getName(), is(GROENTE_FRUIT_AARDAPPELEN));
        assertThat(category.getChildCategories().get(0).getName(), is(GROENTE));
    }

    @Test(expected = IllegalStateException.class)
    // This is happening, because parentCategory is transient, meaning parentCategory does not have an id
    public void addTwoCategoriesPersistChildAndGetIllegalStateException() {
        Category childCategory = createFirstChildCategory(createParentCategory()); // CascadeType.NONE

        categoryDao.persist(childCategory);
    }

    @Test
    public void addMultipleCategories() {
        String[] categoryNames = {GROENTE_FRUIT_AARDAPPELEN, GROENTE, "tomaat-komkommer"};

        Category category = null;
        for (int i = 0; i < categoryNames.length; i++) {
            if (i == 0) {
                category = createParentCategory();
            } else {
                category = new Category(categoryNames[i], category);
            }
            Category categoryToPersist = category;
            categoryDao.persist(categoryToPersist);
        }

        assertThat(categoryDao.getCount(), is(3L));
        assertThat(categoryDao.findReferenceById(3L).getName(), is("tomaat-komkommer"));
    }

    @Test
    public void addDuplicateCategory() {
        Category category = createParentCategory();
        Category duplicateCategory = createParentCategory();

        // Persist should check if the name (unique) exists, if it does, persist will not be performed
        category = categoryDao.persistIfNotExist(category);
        duplicateCategory = categoryDao.persistIfNotExist(duplicateCategory);

        assertThat(categoryDao.getCount(), is(1L));
        assertThat(category.getId(), is(1L));
        assertThat(duplicateCategory.getId(), is(1L));
    }

    @Test
    public void addTwoCategoriesAtOnceManually() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(parentCategory);

        categoryDao.persist(parentCategory);
        categoryDao.persist(childCategory);

        assertThat(categoryDao.getCount(), is(2L));

        Category managedCategory2 = categoryDao.findById(2L);

        assertThat(managedCategory2.getParentCategory(), is(parentCategory));
    }

    @Test
    public void addChildCategoryParentDetached() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(null);

        categoryDao.persist(parentCategory);

        Category managedParentCategory = categoryDao
                .findByPropertyValue("name", createParentCategory().getName()).get(0);
        childCategory.setParentCategory(managedParentCategory);

        categoryDao.persist(childCategory);

        assertThat(categoryDao.getCount(), is(2L));
        assertThat(childCategory.getParentCategory(), is(managedParentCategory));
    }

}
