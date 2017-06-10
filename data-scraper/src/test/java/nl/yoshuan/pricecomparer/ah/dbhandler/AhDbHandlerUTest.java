package nl.yoshuan.pricecomparer.ah.dbhandler;

import nl.yoshuan.pricecomparer.config.TestAppConfig;
import nl.yoshuan.pricecomparer.daos.CategoryDao;
import nl.yoshuan.pricecomparer.entities.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@Transactional
public class AhDbHandlerUTest {

    @Autowired private AhDbHandler ahDbHandler;

    @Test
    public void splitAllCategory() {
        List<Category> splitCategories = ahDbHandler
                .splitFullCategory("/producten/aardappel-groente-fruit/groente/tomaat-paprika-mais/tomaten/trostomaten");

        Category firstCategory = splitCategories.get(0);
        Category secondCategory = splitCategories.get(1);

        assertThat(firstCategory.getName(), is("aardappel-groente-fruit"));
        assertThat(secondCategory.getName(), is("groente"));
        assertThat(secondCategory.getParentCategory(), is(firstCategory));
        assertThat(splitCategories.size(), is(5));
    }

    @Test
    public void splitCategoryOneLevel() {
        List<Category> splitCategories = ahDbHandler
                .splitFullCategory("/producten/aardappel-groente-fruit");

        Category firstCategory = splitCategories.get(0);

        assertThat(firstCategory.getName(), is("aardappel-groente-fruit"));
        assertThat(firstCategory.getChildCategories().size(), is(0));
        assertThat(firstCategory.getParentCategory(), is(nullValue()));
        assertThat(splitCategories.size(), is(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void persistSplitCategories() {
        List<Category> splitCategories = ahDbHandler
                .splitFullCategory("/producten/aardappel-groente-fruit/groente/tomaat-paprika-mais/tomaten/trostomaten");

        ahDbHandler.persistSplitCategories(splitCategories);
        CategoryDao categoryDao = ahDbHandler.getCategoryDao();

        assertThat(categoryDao.getCount(), is(5L));

        categoryDao.clearPersistenceContext();

        Category managedCategory = categoryDao.findById(3L);

        assertThat(managedCategory.getName(), is("tomaat-paprika-mais"));
        assertThat(managedCategory.getChildCategories().get(0).getName(), is("tomaten"));
        assertThat(managedCategory.getParentCategory().getName(), is("groente"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void persistSplitCategoriesFromDifferentUrl() {
        List<Category> splitCategories = ahDbHandler
                .splitFullCategory("/producten/aardappel-groente-fruit/groente/tomaat-paprika-mais/tomaten/trostomaten");

        ahDbHandler.persistSplitCategories(splitCategories);

        List<Category> splitCategories2 = ahDbHandler
                .splitFullCategory("/producten/aardappel-groente-fruit/groente/tomaat-paprika-mais/paprika/rode-paprika");

        ahDbHandler.persistSplitCategories(splitCategories2);

        CategoryDao categoryDao = ahDbHandler.getCategoryDao();
        categoryDao.clearPersistenceContext();

        Category managedCategory = categoryDao.findById(6L);

        assertThat(categoryDao.getCount(), is(7L));
        assertThat(managedCategory.getName(), is("paprika"));
        assertThat(managedCategory.getParentCategory().getName(), is("tomaat-paprika-mais"));
        assertThat(managedCategory.getChildCategories().get(0).getName(), is("rode-paprika"));
    }

}
