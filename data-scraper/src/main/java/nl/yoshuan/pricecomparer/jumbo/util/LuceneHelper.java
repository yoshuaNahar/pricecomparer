package nl.yoshuan.pricecomparer.jumbo.util;

import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import nl.yoshuan.pricecomparer.entities.SimpleProduct;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public abstract class LuceneHelper {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LuceneHelper.class);

    protected final String indexDir;
    protected IndexWriter writer;
    protected IndexSearcher searcher;

    protected LuceneHelper(String indexDir) {
        this.indexDir = indexDir;
    }

    public void addDbDataIntoLuceneIndexes(List<SimpleProduct> dbAhProductWithOnlyNameAndCategoryId) {
        createWriter();
        addDocs(dbAhProductWithOnlyNameAndCategoryId);
        closeWriter();
    }

    public void insertClosestCategoryMatchToProduct(Product product) {
        createSearcher();

        Document doc = getSearchResult(product.getName());
        logger.ifn("Jumbo Product: " + product.getName() + " matched with:" + doc.get("product_name"));

        Category category = new Category(null, null);
        category.setId(Long.parseLong(doc.get("category_id")));
        product.setCategory(category);
    }

    protected abstract void createWriter();

    private void addDocs(List<SimpleProduct> products) {
        for (int i = 0; i < products.size(); i++) {
            Document doc = new Document();
            doc.add(new TextField("product_name", products.get(i).getName(), Field.Store.YES));
            doc.add(new StringField("category_id", "" + products.get(i).getCategoryId(), Field.Store.YES));
            logger.debug("#: " + i + " " + products.get(i).getName());
            try {
                writer.addDocument(doc);
                writer.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeWriter() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void createSearcher();

    private Document getSearchResult(String productName) {
        Document doc = null;
        try {
            Query q = new QueryParser("product_name", new StandardAnalyzer()).parse(QueryParserBase.escape(productName + "~")); // fuzzy search
            // I am adding fuzzy search because of minor differences between ah en jumbo names,
            // like Ah calls their product Bonduelle Bloemkoolroosjes
            // and Jumbo calls their product Bonduelle Bloemkool Roosjes

            TopDocs hits = searcher.search(q, 10);

            logger.debug(hits.totalHits + " docs found for the query \"" + q.toString() + "\"");

            doc = searcher.doc(hits.scoreDocs[0].doc);
            logger.debug(doc.get("product_name") + " " + doc.get("category_id"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return doc;
    }

}
