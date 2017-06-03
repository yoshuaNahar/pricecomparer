package nl.yoshuan.pricecomparer.ah.dbhandler;

import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.ah.util.AhDbHandlerUtil;
import nl.yoshuan.pricecomparer.daos.CategoryDao;
import nl.yoshuan.pricecomparer.daos.ProductDao;
import nl.yoshuan.pricecomparer.daos.ProductVariablesDao;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Transactional
public class AhDbHandler {

    private CategoryDao categoryDao;
    private ProductDao productDao;
    private ProductVariablesDao productVariablesDao;

    @Autowired
    public AhDbHandler(CategoryDao categoryDao, ProductDao productDao, ProductVariablesDao productVariablesDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.productVariablesDao = productVariablesDao;
    }

    // not tested
    public void persistProductsToDb(List<AhProduct> ahProducts) {
        List<AhDbHandlerUtil.DbEntitiesHolder> dbEntitiesHolderList = toDbEntityHolderList(ahProducts);
        HashMap<Integer, Category> allCategories = persistAllCategoriesAndSortProductToCategoryFrom(dbEntitiesHolderList);

        for (int i = 0; i < dbEntitiesHolderList.size(); i++) {
            Product product = dbEntitiesHolderList.get(i).getProduct();
            Category category = allCategories.get(i);
            ProductVariables productVariables = dbEntitiesHolderList.get(i).getProductVariables();

            Category managedCategory = categoryDao.persistIfNotExist(category);
            product.setCategory(managedCategory);
            System.out.println(product);
            productDao.persist(product);
            productVariablesDao.persist(productVariables);
        }
    }

    // not tested
    // terrible method
    HashMap<Integer, Category> persistAllCategoriesAndSortProductToCategoryFrom(List<AhDbHandlerUtil.DbEntitiesHolder> dbEntitiesHolderList) {
        HashMap<Integer, Category> categoryHashMap = new HashMap<>();
        List<String> uniqueFullCategoryNames = new ArrayList<>();

        for (int i = 0; i < dbEntitiesHolderList.size(); i++) {
            String fullCategoryName = dbEntitiesHolderList.get(i).getCategory().getName();
            if(!uniqueFullCategoryNames.contains(fullCategoryName)) { // if (list doesn't contain currentCategoryName)
                uniqueFullCategoryNames.add(fullCategoryName); // add currentCategoryName

                List<Category> splitCategories = AhDbHandlerUtil.splitFullCategory(fullCategoryName); // split the fullCategory
                // aardappel-groente-fruit/groente/... into seperate categories
                categoryHashMap.put(i, splitCategories.get(splitCategories.size() - 1)); // add the last split category (where the
                // products are, to the hashMap
                persistSplitCategories(splitCategories); // persist to db
            } else { // if currentCategoryName already exist
                categoryHashMap.put(i, categoryHashMap.get(i - 1)); // take category from last category in map and add
            }
        }

        return categoryHashMap;
    }

    void persistSplitCategories(List<Category> splitCategories) {
        splitCategories.forEach(category -> System.out.println(category.getClass()));

        // I am doing this, because the categories that exist are in a transient state (dont have their id's set)
        // With this, if a category exist I am putting the existing category in my list
        for (int i = 0; i < splitCategories.size(); i++) {
            if (i > 0) {
                splitCategories.get(i).setParentCategory(splitCategories.get(i - 1));
            }
            splitCategories.set(i, categoryDao.persistIfNotExist(splitCategories.get(i)));
        }
    }

    // for testing only
    CategoryDao getCategoryDao() {
        return categoryDao;
    }

    private List<AhDbHandlerUtil.DbEntitiesHolder> toDbEntityHolderList(List<AhProduct> ahProducts) {
        List<AhDbHandlerUtil.DbEntitiesHolder> dbEntitiesHolderList = new ArrayList<>();
        for (AhProduct ahProduct : ahProducts) {
            dbEntitiesHolderList.add(AhDbHandlerUtil.mapToDbEntities(ahProduct));
        }
        return dbEntitiesHolderList;
    }

}
