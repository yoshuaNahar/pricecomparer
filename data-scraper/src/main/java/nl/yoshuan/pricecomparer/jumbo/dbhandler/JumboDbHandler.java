package nl.yoshuan.pricecomparer.jumbo.dbhandler;

import nl.yoshuan.pricecomparer.daos.ProductDao;
import nl.yoshuan.pricecomparer.daos.ProductVariablesDao;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.ProductVariables;
import nl.yoshuan.pricecomparer.jumbo.entities.JumboProduct;
import nl.yoshuan.pricecomparer.jumbo.util.LuceneHelper;
import nl.yoshuan.pricecomparer.shared.DbEntitiesHolder;
import nl.yoshuan.pricecomparer.shared.DbHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static nl.yoshuan.pricecomparer.entities.ProductVariables.Supermarket.JUMBO;

@Component
@Transactional
public class JumboDbHandler extends DbHandler<JumboProduct> {

    private ProductDao productDao;
    private ProductVariablesDao productVariablesDao;
    private LuceneHelper luceneHelper;

    @Autowired
    public JumboDbHandler(ProductDao productDao, ProductVariablesDao productVariablesDao, LuceneHelper luceneHelper) {
        this.productDao = productDao;
        this.productVariablesDao = productVariablesDao;
        this.luceneHelper = luceneHelper;
    }

    @Override
    public void persistEntitiesToDb(List<JumboProduct> products) {
        persistEntitiesToDb(products, false);
    }

    public void persistEntitiesToDb(List<JumboProduct> jumboProducts, boolean addAhProductsIntoLucene) {
        List<DbEntitiesHolder> dbEntitiesHolderList = toDbEntityHolderList(jumboProducts);

        if (addAhProductsIntoLucene) {
            // this just gets the products from db and adds them to the lucene index
            // but after first persist this is shouldn't be run
            luceneHelper.addDbDataIntoLuceneIndexes(productDao.getAllProductNamesAndCategoryId());
        }

        addCategoriesToProducts(dbEntitiesHolderList);

        for (DbEntitiesHolder dbEntitiesHolder : dbEntitiesHolderList) {
            Product product = dbEntitiesHolder.getProduct();
            ProductVariables productVariables = dbEntitiesHolder.getProductVariables();
            productVariables.setProduct(product);

            productDao.persistIfNotExist(product);
            productVariablesDao.persist(productVariables);
        }
    }

    private void addCategoriesToProducts(List<DbEntitiesHolder> dbEntitiesHolderList) {
        dbEntitiesHolderList.forEach(dbEntitiesHolder ->
                luceneHelper.insertClosestCategoryMatchToProduct(dbEntitiesHolder.getProduct()));
    }

    @Override
    protected DbEntitiesHolder mapToDbEntities(JumboProduct jumboProduct) {
        ProductVariables productVariables =
                new ProductVariables(jumboProduct.getProductSrc()
                        , jumboProduct.getImgUrl()
                        , jumboProduct.getPrice()
                        , jumboProduct.getBonusPrice()
                        , jumboProduct.getBonusType()
                        , jumboProduct.getBonusImg()
                        , JUMBO
                        , null
                        , new Date()
                        , null);

        Product product =
                new Product(jumboProduct.getName()
                        , jumboProduct.getUnitSize()
                        , jumboProduct.getBrandName()
                        , null // category will be inserted later
                        , null);

        // productVariables needs a product ref
        return new DbEntitiesHolder(null, product, productVariables);
    }

}
