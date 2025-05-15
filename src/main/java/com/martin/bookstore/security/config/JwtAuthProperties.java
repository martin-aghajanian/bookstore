package com.martin.bookstore.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtAuthProperties {

    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
    private String authType;
}
