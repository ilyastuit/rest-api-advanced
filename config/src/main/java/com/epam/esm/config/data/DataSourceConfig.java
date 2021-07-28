package com.epam.esm.config.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${datasource.driver}")
    private String datasourceDriver;
    @Value("${datasource.url}")
    private String datasourceUrl;
    @Value("${datasource.username}")
    private String datasourceUsername;
    @Value("${datasource.password}")
    private String datasourcePassword;


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(this.datasourceDriver);
        dataSource.setUrl(this.datasourceUrl);
        dataSource.setUsername(this.datasourceUsername);
        dataSource.setPassword(this.datasourcePassword);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public TransactionTemplate transactionTemplate() {
        PlatformTransactionManager transactionManager = new JdbcTransactionManager(dataSource());
        return new TransactionTemplate(transactionManager);
    }
}
