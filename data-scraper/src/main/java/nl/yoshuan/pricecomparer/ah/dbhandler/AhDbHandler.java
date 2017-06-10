package nl.yoshuan.pricecomparer.ah.dbhandler;

import nl.yoshuan.pricecomparer.ah.entities.AhProduct;
import nl.yoshuan.pricecomparer.daos.CategoryDao;
import nl.yoshuan.pricecomparer.daos.ProductDao;
import nl.yoshuan.pricecomparer.daos.ProductVariablesDao;
import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import nl.yoshuan.pricecomparer.shared.DbEntitiesHolder;
import nl.yoshuan.pricecomparer.shared.DbHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static nl.yoshuan.pricecomparer.entities.ProductVariables.Supermarket.AH;

@Component
@Transactional
public class AhDbHandler extends DbHandler<AhProduct> {

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
    @Override
    public void persistEntitiesToDb(List<AhProduct> ahProducts) {
        List<DbEntitiesHolder> dbEntitiesHolderList = toDbEntityHolderList(ahProducts);
        HashMap<Integer, Category> allCategories = persistAllCategoriesAndSortProductToCategory(dbEntitiesHolderList);

        for (int i = 0; i < dbEntitiesHolderList.size(); i++) {
            Product product = dbEntitiesHolderList.get(i).getProduct();
            Category category = allCategories.get(i);
            ProductVariables productVariables = dbEntitiesHolderList.get(i).getProductVariables();

            Category managedCategory = categoryDao.persistIfNotExist(category);
            product.setCategory(managedCategory);

            Product managedProduct = productDao.persistIfNotExist(product);
            productVariables.setProduct(managedProduct);

            productVariablesDao.persist(productVariables);
        }
    }

    // not tested
    // methods should not take so long to understand
    HashMap<Integer, Category> persistAllCategoriesAndSortProductToCategory(List<DbEntitiesHolder> dbEntitiesHolderList) {
        HashMap<Integer, Category> categoryHashMap = new HashMap<>();
        List<String> uniqueFullCategoryNames = new ArrayList<>();

        for (int i = 0; i < dbEntitiesHolderList.size(); i++) {
            String fullCategoryName = dbEntitiesHolderList.get(i).getCategory().getName();
            if (!uniqueFullCategoryNames.contains(fullCategoryName)) { // if (list doesn't contain currentCategoryName)
                uniqueFullCategoryNames.add(fullCategoryName); // add currentCategoryName

                List<Category> splitCategories = splitFullCategory(fullCategoryName); // split the fullCategory
                // aardappel-groente-fruit/groente/... into seperate categories
                categoryHashMap.put(i, splitCategories.get(splitCategories.size() - 1)); // add the last split category (category where
                // the ah products in, to the hashMap
                persistSplitCategories(splitCategories); // persist to db
            } else { // if currentCategoryName already exist
                categoryHashMap.put(i, categoryHashMap.get(i - 1)); // take category from last category in map and add
            }
        }

        return categoryHashMap;
    }

    void persistSplitCategories(List<Category> splitCategories) {
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

    List<Category> splitFullCategory(String fullCategoryName) {
        // the full category will usually be of this type, so I remove the first producten substring
        // /producten/aardappel-groente-fruit/groente/tomaat-komkommer/tomaten/trostomaten
        String fullCategoryNameExceptProducten = fullCategoryName.replace("/producten/", "");

        String[] categoryNames = fullCategoryNameExceptProducten.split(Pattern.quote("/")); // split on /

        List<Category> linkedCategories = new ArrayList<>();

        for (String categoryName : categoryNames) {
            linkedCategories.add(new Category(categoryName, null));
        }

        for (int i = 1; i < linkedCategories.size(); i++) { // add parent to child, so that db parent_id column is not null
            linkedCategories.get(i).setParentCategory(linkedCategories.get(i - 1));
        }

        return linkedCategories;
    }

    @Override
    protected DbEntitiesHolder mapToDbEntities(AhProduct ahProduct) {
        // not gonna use ahProduct.getCategoryName()
        Category category = new Category(ahProduct.getFullCategoryName(), null);

        if (ahProduct.getDiscount() == null) {
            ahProduct.setDiscount(new AhProduct.Discount(null));
        }
        if (ahProduct.getPriceLabel().getPriceWas() == null) {
            ahProduct.getPriceLabel().setPriceWas("0");
        }
        if (ahProduct.getPropertyIcons() == null) {
            ahProduct.setPropertyIcons(new ArrayList<>());
        }

        ProductVariables productVariables =
                new ProductVariables(ahProduct.getProductSrc()
                        , ahProduct.getImageSrc()
                        , (int) (Double.parseDouble(ahProduct.getPriceLabel().getPriceNow()) * 100)
                        , (int) (Double.parseDouble(ahProduct.getPriceLabel().getPriceWas()) * 100)
                        , ahProduct.getDiscount().getLabel()
                        , ahProduct.getDiscountImageSrc()
                        , AH
                        , ahProduct.getPropertyIcons().toString()
                        , new Date()
                        , null);

        Product product =
                new Product(ahProduct.getName()
                        , ahProduct.getUnitSize()
                        , ahProduct.getBrandName()
                        , category
                        , null); // dont add the productVariable,
        // You will get a TransientPropertyValueException, because you are trying to
        // persist an object that has a reference to a transient object (productVariable)

        // Some AH products like, https://www.ah.nl/producten/product/wi233066/tasty-tom-gold has no brand name!
        if (product.getBrand() == null) {
            product.setBrand("AH");
        }

        return new DbEntitiesHolder(category, product, productVariables);
    }

}
