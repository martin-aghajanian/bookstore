package com.martin.bookstore.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

@Getter
@Setter
@ConfigurationProperties(prefix = "validation.password")
@Primary
public class PasswordValidationProperties {
    private String pattern;
}
