package ru.aovechnikov.voting.configuration;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.USE_SQL_COMMENTS;

/**
 * Created rootContext {@link ApplicationContext}  that is
 * used by the Spring IoC container as a source of bean definitions for repository layer.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
@Configuration
@PropertySource(value = "classpath:db/hsqldb.properties")
@EnableJpaRepositories("ru.aovechnikov.voting.repository")
@ComponentScan(basePackages = "ru.aovechnikov.voting.repository")
@EnableTransactionManagement
public class DbConfiguration {

    @Autowired
    private Environment env;

    /**
     * Singleton bean that defines the implementation of the source of
     * database connections used by the application.
     */
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("database.driverClassName"));
        dataSource.setUrl(env.getProperty("database.url"));
        dataSource.setUsername(env.getProperty("database.username"));
        dataSource.setPassword(env.getProperty("database.password"));
        return dataSource;
    }

    /**
     * Singleton {@link FactoryBean} that creates a JPA {@link EntityManagerFactory}
     * according to JPA  standard container bootstrap contract.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("ru.aovechnikov.voting.model");
        emf.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(Boolean.parseBoolean(env.getProperty("jpa.showSql")));
        emf.setJpaVendorAdapter(adapter);
        Properties properties = new Properties();
        properties.setProperty(FORMAT_SQL, env.getProperty("hibernate.format_sql"));
        properties.setProperty(USE_SQL_COMMENTS, "hibernate.use_sql_comments");
        emf.setJpaProperties(properties);
        return emf;
    }


    /**
     * Singleton bean for transactional data access.
     * @param entityManagerFactory the EntityManagerFactory
     */
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource());
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    /**
     * Initialize and populate dataSource
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(){
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource());
        initializer.setEnabled(Boolean.parseBoolean(env.getProperty("database.init")));
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("db/" + env.getProperty("jdbc.initLocation")));
        populator.addScript(new ClassPathResource("db/populateDB.sql"));
        populator.setSqlScriptEncoding("UTF-8");
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
