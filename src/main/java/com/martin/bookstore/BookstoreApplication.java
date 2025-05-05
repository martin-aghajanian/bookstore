package com.martin.bookstore;

import com.martin.bookstore.security.auth.AuthenticationService;
import com.martin.bookstore.security.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.martin.bookstore.security.user.Role.ADMIN;
import static com.martin.bookstore.security.user.Role.MANAGER;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

}
