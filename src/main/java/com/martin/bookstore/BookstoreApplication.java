package com.martin.bookstore;

import com.martin.bookstore.security.config.JwtAuthProperties;
import com.martin.bookstore.security.properties.PasswordValidationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(
        {
                PasswordValidationProperties.class,
                JwtAuthProperties.class
        }
)
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

}
