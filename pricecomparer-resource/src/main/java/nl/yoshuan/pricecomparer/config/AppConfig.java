package nl.yoshuan.pricecomparer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"nl.yoshuan.pricecomparer.controllers", "nl.yoshuan.pricecomparer.services"})
@PropertySource("classpath:app.properties")
@Import(DbConfig.class)
public class AppConfig {
}
