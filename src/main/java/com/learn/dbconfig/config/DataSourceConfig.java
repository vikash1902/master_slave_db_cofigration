package com.learn.dbconfig.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;


@Configuration
@EnableJpaRepositories(basePackages = "com.learn.dbconfig.repository")
@EntityScan(basePackages = "com.learn.dbconfig.entity")
@EnableAspectJAutoProxy
public class DataSourceConfig {

    @Bean
    @Qualifier("masterDataSource")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/master")
                .username("postgres")
                .password("root")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    @Qualifier("slaveDataSource")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/slave")
                .username("postgres")
                .password("root")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        DynamicDataSource dataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER.getType(), masterDataSource);
        targetDataSources.put(DataSourceType.SLAVE.getType(), slaveDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource routingDataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(routingDataSource);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setPackagesToScan("com.learn.dbconfig.entity"); // Adjust this to your entity package
        return factoryBean;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource routingDataSource) {
        return new DataSourceTransactionManager(routingDataSource);
    }
}
