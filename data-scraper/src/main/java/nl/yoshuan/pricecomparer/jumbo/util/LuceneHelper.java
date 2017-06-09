package nl.yoshuan.pricecomparer.jumbo.util;

import nl.yoshuan.pricecomparer.entities.Category;
import nl.yoshuan.pricecomparer.entities.Product;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Component
public class LuceneHelper {

    // TODO: I want this class to:
    // 1. take ALL data from the database and put that data into lucene indexes (only add the name and the category id)
    // 2. scrape Jumbo and for every found record check the closest match inside the lucene index (only check the name)
    // 3. from that found product take the category id and add it to the Jumbo product class so that it will be placed inside the same category;

    // I first need to add all ah products before I can use this class, because I need all ah products to form the closest match to other products

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LuceneHelper.class);

    private static final String INDEX_DIR = "/home/yoshua/Documents/IdeaProjects/pricecomparer/lucene6idx";
    private IndexWriter writer;
    private IndexSearcher searcher;

    public void addDbDataIntoLuceneIndexes(List<Product> dbAhProductWithOnlyNameAndCategoryId) {
        createWriter();
        addDocs(dbAhProductWithOnlyNameAndCategoryId);
        closeWriter();
    }

    // possibly return the same product with only the categoyr set
    public Product checkClosestMatchFromIndex(Product jumboProduct) {
        createSearcher();

        Document doc = getSearchResult(jumboProduct.getName());
        logger.debug("Jumbo Product: " + jumboProduct.getName() + " matched with:" + doc.get("product_name"));

        Category category = new Category(null, null);
        category.setId(Long.parseLong(doc.get("category_id")));
        jumboProduct.setCategory(category);

        return jumboProduct;
    }

    private void createWriter() {
        try {
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            Directory index = FSDirectory.open(Paths.get(INDEX_DIR));
            writer = new IndexWriter(index, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDocs(List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            Document doc = new Document();
            doc.add(new TextField("product_name", products.get(i).getName(), Field.Store.YES));
            doc.add(new StringField("category_id", "" + products.get(i).getCategory().getId(), Field.Store.YES));
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

    private void createSearcher() {
        try {
            Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
            IndexReader reader = DirectoryReader.open(dir);
            searcher = new IndexSearcher(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document getSearchResult(String productName) {
        Document doc = null;
        try {
            Query q = new QueryParser("product_name", new StandardAnalyzer()).parse(productName + "~"); // fuzzy search
            // I am adding fuzzy search because of minor differences between ah en jumbo,
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
