package nl.yoshuan.pricecomparer.dbhandler;

import nl.yoshuan.pricecomparer.entities.Category;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DbHandlerUTest {


    @Before
    public void setup() {
    }

    @Test
    public void splitFullCategory() {
        List<Category> splitCategories = DbHandler
                .splitFullCategory("/producten/aardappel-groente-fruit/groente/tomaat-komkommer/tomaten/trostomaten");

        Category firstCategory = splitCategories.get(0);
        Category secondCategory = splitCategories.get(1);

        assertThat(firstCategory.getCategoryName(), is("aardappel-groente-fruit"));
        assertThat(secondCategory.getCategoryName(), is("groente"));
        assertThat(secondCategory.getParentCategory(), is(firstCategory));
        assertThat(splitCategories.size(), is(5));
    }

}
