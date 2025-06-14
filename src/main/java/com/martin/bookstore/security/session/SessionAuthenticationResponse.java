package com.martin.bookstore.security.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionAuthenticationResponse {
    private String sessionId;
    private Long userId;
    private String username;
}
