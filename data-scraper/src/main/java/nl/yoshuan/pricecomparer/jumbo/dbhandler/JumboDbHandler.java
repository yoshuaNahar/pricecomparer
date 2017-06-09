package nl.yoshuan.pricecomparer.jumbo.dbhandler;

import nl.yoshuan.pricecomparer.daos.CategoryDao;
import nl.yoshuan.pricecomparer.daos.ProductDao;
import nl.yoshuan.pricecomparer.daos.ProductVariablesDao;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import nl.yoshuan.pricecomparer.jumbo.util.JumboDbHandlerUtil;
import nl.yoshuan.pricecomparer.jumbo.util.LuceneHelper;
import nl.yoshuan.pricecomparer.util.DbEntitiesHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class JumboDbHandler {

    private CategoryDao categoryDao;
    private ProductDao productDao;
    private ProductVariablesDao productVariablesDao;
    private LuceneHelper luceneHelper;

    @Autowired
    public JumboDbHandler(CategoryDao categoryDao, ProductDao productDao, ProductVariablesDao productVariablesDao, LuceneHelper luceneHelper) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.productVariablesDao = productVariablesDao;
        this.luceneHelper = luceneHelper;
    }

    public void persistEntitiesToDb(List<JumboProduct> jumboProducts) {
        List<DbEntitiesHolder> dbEntitiesHolderList = toDbEntityHolderList(jumboProducts);

        addCategoryToProducts(dbEntitiesHolderList);

        for (DbEntitiesHolder dbEntitiesHolder : dbEntitiesHolderList) {
            Product product = dbEntitiesHolder.getProduct();
            ProductVariables productVariables = dbEntitiesHolder.getProductVariables();
            productVariables.setProduct(product);

            productDao.persistIfNotExist(product);
            productVariablesDao.persist(productVariables);
        }
    }

    private void addCategoryToProducts(List<DbEntitiesHolder> dbEntitiesHolderList) {
        luceneHelper.addDbDataIntoLuceneIndexes(productDao.getAllProductNamesAndCategoryId()); // TODO: Write this class method

        dbEntitiesHolderList.forEach(dbEntitiesHolder -> {
            luceneHelper.checkClosestMatchFromIndex(dbEntitiesHolder.getProduct());

            // something like this
            Product product = dbEntitiesHolder.getProduct();
            productDao.persistIfNotExist(product);

            ProductVariables productVariables = dbEntitiesHolder.getProductVariables();
            productVariables.setProduct(product);
            productVariablesDao.persist(productVariables);
        });
    }

    private List<DbEntitiesHolder> toDbEntityHolderList(List<JumboProduct> jumboProducts) {
        List<DbEntitiesHolder> dbEntitiesHolderList = new ArrayList<>();

        for (JumboProduct jumboProduct : jumboProducts) {
            dbEntitiesHolderList.add(JumboDbHandlerUtil.mapToDbEntities(jumboProduct));
        }

        return dbEntitiesHolderList;
    }


}
