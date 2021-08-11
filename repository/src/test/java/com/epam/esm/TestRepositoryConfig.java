package com.epam.esm;

import com.epam.esm.TestEnvironment;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.order.Order;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.user.User;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.repository.order.OrderRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.repository.user.UserRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.epam.esm.repository.*", "com.epam.esm.entity.*"})
public class TestRepositoryConfig {


    public static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        FileReader file = new FileReader(TestEnvironment.class.getResource("/application-test.properties").getFile());
        properties.load(file);
        return properties;
    }

    @Bean
    public DataSource dataSource() throws IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Properties properties = getProperties();
        dataSource.setDriverClassName(properties.getProperty("datasource.driver"));
        dataSource.setUrl(properties.getProperty("datasource.url"));
        dataSource.setUsername(properties.getProperty("datasource.username"));
        dataSource.setPassword(properties.getProperty("datasource.password"));

        return dataSource;
    }


    @Bean
    public Flyway getFlyway() throws IOException {
        org.flywaydb.core.api.configuration.Configuration configuration = new FluentConfiguration()
                .dataSource(dataSource())
                .configuration(getProperties());
        return new Flyway(configuration);
    }

}
