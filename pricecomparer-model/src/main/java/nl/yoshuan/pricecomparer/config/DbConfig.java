package nl.yoshuan.pricecomparer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "nl.yoshuan.pricecomparer.daos")
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
public class DbConfig {

    @Autowired
    private Environment env;

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(env.getProperty("db.user"));
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        return dataSource;
    }

    // Read up on the link below as to why I created my emf this way
    // http://docs.spring.io/spring/docs/4.3.8.RELEASE/spring-framework-reference/htmlsingle/#orm-jpa-setup-lcemfb
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("nl.yoshuan.pricecomparer.entities"); // getClass().getPackage().getName() --> nl.yoshuan.pricecomparer.config
        factory.setDataSource(dataSource());

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
        jpaProperties.put("hibernate.show_sql", true);
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("hibernate.use_sql_comments", true);
        jpaProperties.put("hibernate.hbm2ddl.auto", "validate");
        factory.setJpaPropertyMap(jpaProperties);

        return factory;
    }

    // This is used with my nl.yoshua.test.dao's and aop magic, so that I don't have to begin and commit
    // and rollback transactions manually.
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

}
