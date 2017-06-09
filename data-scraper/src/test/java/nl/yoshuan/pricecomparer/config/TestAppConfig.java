package nl.yoshuan.pricecomparer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"nl.yoshuan.pricecomparer.ah", "nl.yoshuan.pricecomparer.jumbo"})
@Import(TestDbConfig.class)
public class TestAppConfig {

}
