package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class RestApiAdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiAdvancedApplication.class, args);
    }

}