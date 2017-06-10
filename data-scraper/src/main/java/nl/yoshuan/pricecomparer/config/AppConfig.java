package nl.yoshuan.pricecomparer.config;

import nl.yoshuan.pricecomparer.jumbo.util.LuceneHelper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
@ComponentScan(basePackages = {"nl.yoshuan.pricecomparer.ah", "nl.yoshuan.pricecomparer.jumbo"})
@Import(DbConfig.class)
public class AppConfig {

    // Doing the implementation here feels wrong, but this IS the place to choose
    // which implementation you want to use. My test Config will have a different implementation; and
    // the actual code that is used in JumboDbHandler stays the same.
    // The reason I'm doing this is to make luceneHelper testable with a RAMDirectory. Creating two implementations
    // classes, one for prod and one for test (which I will never use in prod) also feels incorrect.
    @Bean
    public LuceneHelper luceneHelper() {
        return new LuceneHelper("/home/yoshua/Documents/IdeaProjects/pricecomparer/lucene6idx") {
            // wtf
            @Override
            protected void createWriter() {
                try {
                    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
                    Directory dir = FSDirectory.open(Paths.get(indexDir));
                    writer = new IndexWriter(dir, config);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void createSearcher() {
                try {
                    Directory dir = FSDirectory.open(Paths.get(indexDir));
                    IndexReader reader = DirectoryReader.open(dir);
                    searcher = new IndexSearcher(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }

}
