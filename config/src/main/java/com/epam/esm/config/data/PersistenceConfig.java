package com.epam.esm.config.data;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "com.epam.esm.repository.*")
@EnableTransactionManagement
public class PersistenceConfig {
}
