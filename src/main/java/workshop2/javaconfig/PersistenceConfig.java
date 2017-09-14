/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.javaconfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.persistencelayer.ProductService;
import workshop2.persistencelayer.hibernate.ProductServiceHibernate;

/**
 *
 * @author hwkei
 */
@Configuration
@ComponentScan(basePackages={"workshop2.interfacelayer", "workshop2.persistencelayer"})
@EnableTransactionManagement
public class PersistenceConfig {
    
    @Bean("ProductServiceBean")
    public ProductService getProductService() {
        String persistenceProvider = DatabaseConnection.getInstance().getPersistenceProvider();
        switch (persistenceProvider) {
            case "hibernate":
                return new ProductServiceHibernate();
            default: {
                System.out.println("Geen persistentie toeleveraar gevonden. Neem contact op met de helpdesk!");
                return null;
            }
        }
    }
    
    @Bean  
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
             JpaVendorAdapter jpaVendorAdapter, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        
        emfb.setDataSource(dataSource);
        emfb.setJpaVendorAdapter(jpaVendorAdapter);
        emfb.setJpaDialect(new HibernateJpaDialect());
        emfb.setJpaProperties(jpaProperties());
        
       
        emfb.setPersistenceUnitName("Hibernate");
        // Geen idee waarom de volgende regel niet werkt???!!!
        emfb.setPackagesToScan("workshop2.domain");
        return emfb;
    }

    
    private Properties jpaProperties() {
        Properties jpaProps = new Properties();
        jpaProps.setProperty("hibernate.hbm2ddl.auto", "create-drop");
       
        return jpaProps;
    }
    
//        private HikariConfig hikariConfig() throws IOException{        
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setPoolName("springHikariCP");
//       
//        
//        hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
//        hikariConfig.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/applikaasie2?createDatabaseIfNotExist=true");
//       hikariConfig.addDataSourceProperty("user", "root");
//       hikariConfig.addDataSourceProperty("password", "mysql");
//        
//        return hikariConfig;
//    }
    
//    @Bean
//    public HikariDataSource hikariDataSource() throws IOException{
//        System.out.println("Creating HikariDatasource bean.");
//        return new HikariDataSource(hikariConfig());
//    }


    @Bean
    public DataSource basicDataSource() {
        DriverManagerDataSource bds = new DriverManagerDataSource();//new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/applikaasie2?createDatabaseIfNotExist=true");
        bds.setUsername("testuser");
        bds.setPassword("Testen01!");
        return bds;
    }
    
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setShowSql(true);
        adapter.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "create");
        //adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        return adapter;
    }
    
    @Bean
    public PersistenceAnnotationBeanPostProcessor paPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }
    
    @Bean
    public BeanPostProcessor persistenceTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }    
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    
}

