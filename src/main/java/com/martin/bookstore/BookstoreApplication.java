package com.martin.bookstore;

import com.martin.bookstore.security.config.JwtAuthProperties;
import com.martin.bookstore.security.properties.PasswordValidationProperties;
import com.martin.bookstore.security.session.SessionAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableConfigurationProperties(
        {
                PasswordValidationProperties.class,
                JwtAuthProperties.class,
                SessionAuthProperties.class
        }
)
@EnableScheduling
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

}
