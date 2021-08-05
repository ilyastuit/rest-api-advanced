package com.epam.esm.cli;

import com.epam.esm.cli.infrastructure.param.handler.Handler;
import com.epam.esm.cli.infrastructure.param.resolver.Resolver;
import com.epam.esm.cli.infrastructure.param.exception.HandlerNotFoundException;
import com.epam.esm.cli.infrastructure.param.exception.InvalidCommandException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.epam.esm.cli.*", "com.epam.esm.config.data"})
public class RestApiAdvancedConsoleApplication implements CommandLineRunner {

    private final Resolver resolver;

    public RestApiAdvancedConsoleApplication(Resolver resolver) {
        this.resolver = resolver;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RestApiAdvancedConsoleApplication.class);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        for (String path : args) {
            try {
                resolver.resolve(path).handle();
            } catch (HandlerNotFoundException | InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
