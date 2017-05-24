package nl.yoshuan.pricecomparer.dao;

import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.utils.DaoTestSetup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static nl.yoshuan.pricecomparer.utils.CategoryUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class CategoryDaoUTest extends DaoTestSetup {

    // I added tests for the methods I will definitely use.
    // Only add more tests if I want to try out a specific feature or if I encountered a bug.
    private CategoryDao categoryDao;

    @Before
    public void setup() {
        initializeTestDB();

        categoryDao = new CategoryDaoImpl();
        categoryDao.setEntityManager(em);
    }

    @After
    public void cleanUp() {
        closeEntityManager();
    }

    @Test
    public void addCategoryAndFindIt() {
        Category parentCategory = createParentCategory();
        dbCommandExecutor.executeCommand(() -> categoryDao.persist(parentCategory));
        Category category = categoryDao.findReferenceById(1L);

        assertThat(categoryDao.getCount(), is(1L));
        assertThat(category.getCategoryName(), is(GROENTE_FRUIT_AARDAPPELEN));
        assertThat(category.getParentCategory(), is(nullValue()));
    }

    @Test
    public void findCategoryIdNotFound() {
        assertThat(categoryDao.findById(999L), is(nullValue()));
    }

    @Test
    public void addTwoCategoriesAndFindBoth() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(parentCategory);

        dbCommandExecutor.executeCommand(() -> categoryDao.persist(parentCategory));
        dbCommandExecutor.executeCommand(() -> categoryDao.persist(childCategory));

        Category category = categoryDao.findReferenceById(2L);

        assertThat(categoryDao.getCount(), is(2L));
        assertThat(category.getCategoryName(), is(GROENTE));
        assertThat(category.getParentCategory().getCategoryName(), is(GROENTE_FRUIT_AARDAPPELEN));
        assertThat(category.getParentCategory().getParentCategory(), is(nullValue()));
    }

    @Test
    public void addTwoCategoriesPersistParentAndFindBoth() {
        Category parentCategory = createParentCategory();
        parentCategory.getChildCategories().add(new Category(GROENTE, parentCategory));

        dbCommandExecutor.executeCommand(() -> categoryDao.persist(parentCategory));

        Category category = categoryDao.findReferenceById(1L);

        assertThat(categoryDao.getCount(), is(2L));
        assertThat(category.getCategoryName(), is(GROENTE_FRUIT_AARDAPPELEN));
        assertThat(category.getChildCategories().get(0).getCategoryName(), is(GROENTE));
    }

    @Test(expected = IllegalStateException.class)
    public void addTwoCategoriesPersistChildAndGetIllegalStateException() {
        Category childCategory = createFirstChildCategory(createParentCategory());
        // This is happening, because parentCategory is transient, meaning parentCategory does not have an id

        dbCommandExecutor.executeCommand(() -> categoryDao.persist(childCategory));
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
            dbCommandExecutor.executeCommand(() -> categoryDao.persist(categoryToPersist));
        }

        assertThat(categoryDao.getCount(), is(3L));
        assertThat(categoryDao.findReferenceById(3L).getCategoryName(), is("tomaat-komkommer"));
    }

    @Test
    public void persistDuplicateCategory() {
        Category category = createParentCategory();
        Category duplicateCategory = createParentCategory();

        // Persist should check if the name (unique) exists, if it does, persist will not be performed
        dbCommandExecutor.executeCommand(() -> categoryDao.persistIfNotExist(category));
        dbCommandExecutor.executeCommand(() -> categoryDao.persistIfNotExist(duplicateCategory));

        assertThat(categoryDao.getCount(), is(1L));
        assertThat(category.getId(), is(1L));
        assertThat(duplicateCategory.getId(), is(nullValue()));
    }

    @Test
    public void persistTwoCategoriesAtOnceManually() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(parentCategory);

        categoryDao.getEntityManager().getTransaction().begin();
        categoryDao.persist(parentCategory);
        categoryDao.persist(childCategory);
        categoryDao.getEntityManager().getTransaction().commit();

        assertThat(categoryDao.getCount(), is(2L));
    }

    @Test
    public void persistChildCategoryParentDetached() {
        Category parentCategory = createParentCategory();
        Category childCategory = createFirstChildCategory(createParentCategory());

        dbCommandExecutor.executeCommand(() -> categoryDao.persist(parentCategory));

//        Category managedParentCategory = categoryDao.

//        dbCommandExecutor.executeCommand(() -> categoryDao.persist(childCategory));
    }

}
