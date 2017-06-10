package nl.yoshuan.pricecomparer.config;

import nl.yoshuan.pricecomparer.jumbo.util.LuceneHelper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = {"nl.yoshuan.pricecomparer.ah", "nl.yoshuan.pricecomparer.jumbo"})
@Import(TestDbConfig.class)
public class TestAppConfig {

    @Bean
    public LuceneHelper luceneHelper() {
        return new LuceneHelper(null) {

            private Directory dir;

            @Override
            protected void createWriter() {
                try {
                    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
                    dir = new RAMDirectory();
                    writer = new IndexWriter(dir, config);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void createSearcher() {
                try {
                    IndexReader reader = DirectoryReader.open(dir);
                    searcher = new IndexSearcher(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }

}
