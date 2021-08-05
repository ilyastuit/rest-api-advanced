package com.epam.esm;

import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.repository.user.UserRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TestEnvironment {

    public static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        FileReader file = new FileReader(TestEnvironment.class.getResource("/application-test.properties").getFile());
        properties.load(file);
        return properties;
    }

    public static DataSource getDataSource() throws IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Properties properties = getProperties();
        dataSource.setDriverClassName(properties.getProperty("datasource.driver"));
        dataSource.setUrl(properties.getProperty("datasource.url"));
        dataSource.setUsername(properties.getProperty("datasource.username"));
        dataSource.setPassword(properties.getProperty("datasource.password"));

        return dataSource;
    }

    public static UserRepository getUserRepository() throws IOException {
        return new UserRepository(
                getJdbcTemplate()
        );
    }

    public static GiftCertificateRepository getGiftCertificateRepository() throws IOException {
        return new GiftCertificateRepository(
                getJdbcTemplate(),
                getNamedParameterJdbcTemplate(),
                getTransactionTemplate(),
                getTagRepository());
    }

    public static TagRepository getTagRepository() throws IOException {
        return new TagRepository(
                getNamedParameterJdbcTemplate(),
                getJdbcTemplate(),
                getTransactionTemplate()
        );
    }

    public static OrderRepository getOrderRepository() throws IOException {
        return new OrderRepository(
                getNamedParameterJdbcTemplate(),
                getJdbcTemplate(),
                getUserRepository(),
                getGiftCertificateRepository()
        );
    }

    public static Flyway getFlyway() throws IOException {
        Configuration configuration = new FluentConfiguration()
                .dataSource(getDataSource())
                .configuration(getProperties());
        return new Flyway(configuration);
    }

    public static JdbcTemplate getJdbcTemplate() throws IOException {
        return new JdbcTemplate(getDataSource());
    }

    public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() throws IOException {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    public static TransactionTemplate getTransactionTemplate() throws IOException {
        PlatformTransactionManager transactionManager = new JdbcTransactionManager(getDataSource());
        return new TransactionTemplate(transactionManager);
    }

}
