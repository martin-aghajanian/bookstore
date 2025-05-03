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

//    @Bean
//    public CommandLineRunner commandLineRunner(
//            AuthenticationService service
//    ) {
//        return args -> {
//            var admin = RegisterRequest.builder()
//                    .firstName("admin")
//                    .lastName("admin")
//                    .email("admin@mail.com")
//                    .username("admin")
//                    .password("password")
//                    .role(ADMIN)
//                    .build();
//            System.out.println("admin token: " + service.register(admin).getAccessToken());
//
//            var manager = RegisterRequest.builder()
//                    .firstName("manager")
//                    .lastName("manager")
//                    .username("manager")
//                    .email("manager@mail.com")
//                    .password("password")
//                    .role(MANAGER)
//                    .build();
//            System.out.println("Manager token: " + service.register(manager).getAccessToken());
//
//        };
//    }
}
