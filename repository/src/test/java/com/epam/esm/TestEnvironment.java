package com.epam.esm;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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

    public static Flyway getFlyway() throws IOException {
        Configuration configuration = new FluentConfiguration()
                .dataSource(getDataSource())
                .configuration(getProperties());
        return new Flyway(configuration);
    }

}
