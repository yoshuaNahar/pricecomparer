package nl.yoshuan.pricecomparer;

import nl.yoshuan.pricecomparer.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(AppConfig.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

}
