package nl.yoshuan.pricecomparer.daos;

import nl.yoshuan.pricecomparer.config.TestDbConfig;
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
@ContextConfiguration(classes = TestDbConfig.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Link below for why I need this...
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-resetting-auto-increment-columns-before-each-test-method/
public class CategoryDaoITest {
    // I didnt use TDD when developing this part. because these aren't
    // even unit tests since I include the db.

    // I only added tests for the methods I will definitely use.
    // Only add more tests if I want to try out a specific feature or if I encountered a bug.

    @Autowired private CategoryDao categoryDao;

    @Test
    public void addCategoryAndFindIt() {
        Category category = createParentCategory();
        categoryDao.persist(category);

        categoryDao.clearPersistenceContext();

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

        categoryDao.clearPersistenceContext();

        Category category = categoryDao.findReferenceById(2L);

        assertThat(categoryDao.getCount(), is(2L));
        assertThat(category.getName(), is(GROENTE));
        assertThat(category.getParentCategory().getName(), is(GROENTE_FRUIT_AARDAPPELEN));
        assertThat(category.getParentCategory().getParentCategory(), is(nullValue()));
    }

    @Test
    public void addTwoCategoriesPersistParentAndFindOnlyParent() {
        Category parentCategory = createParentCategory();
        parentCategory.getChildCategories().add(new Category(GROENTE, parentCategory)); // CascadeType.NONE

        categoryDao.persist(parentCategory);

        categoryDao.clearPersistenceContext();

        Category category = categoryDao.findReferenceById(1L);

        assertThat(categoryDao.getCount(), is(1L));
        assertThat(category.getName(), is(GROENTE_FRUIT_AARDAPPELEN));
        assertThat(category.getChildCategories().size(), is(0));
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

        categoryDao.clearPersistenceContext();

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

        categoryDao.clearPersistenceContext();

        assertThat(categoryDao.getCount(), is(2L));

        Category managedCategory2 = categoryDao.findById(2L);

        assertThat(managedCategory2.getParentCategory().getId(), is(parentCategory.getId()));
        assertThat(managedCategory2.getParentCategory().getName(), is(parentCategory.getName()));
    }

    @Test
    public void addChildCategoryParentDetached() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(null);

        categoryDao.persist(parentCategory);
        categoryDao.clearPersistenceContext();

        Category managedParentCategory = categoryDao
                .findByPropertyValue("name", createParentCategory().getName()).get(0);
        childCategory.setParentCategory(managedParentCategory);

        categoryDao.persist(childCategory);

        assertThat(categoryDao.getCount(), is(2L));
        assertThat(childCategory.getParentCategory(), is(managedParentCategory));
    }

    @Test
    public void add3LevelsOfRelatedCategoriesPersistAllManually() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(null);
        Category grandChildCategory = new Category("tomaat", null);

        // so that the children parent id column is not null
        childCategory.setParentCategory(parentCategory);
        grandChildCategory.setParentCategory(childCategory);

        categoryDao.persist(parentCategory);
        categoryDao.persist(childCategory);
        categoryDao.persist(grandChildCategory);

        assertThat(categoryDao.getCount(), is(3L));
        assertThat(parentCategory.getId(), is(1L));
        assertThat(grandChildCategory.getId(), is(3L));

        categoryDao.clearPersistenceContext();
        Category managedChildCategory = categoryDao.findById(2L);

        assertThat(managedChildCategory.getParentCategory().getId(), is(parentCategory.getId()));
        assertThat(managedChildCategory.getParentCategory().getName(), is(parentCategory.getName()));

        assertThat(managedChildCategory.getChildCategories().get(0).getId(), is(grandChildCategory.getId()));
        assertThat(managedChildCategory.getChildCategories().get(0).getName(), is(grandChildCategory.getName()));
    }

}
