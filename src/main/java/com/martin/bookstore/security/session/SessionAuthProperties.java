package com.martin.bookstore.security.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.session")
public class SessionAuthProperties {
    private String headerName;
    private int expirationMinutes;
}
