package com.choicemanager.payload;

import lombok.Getter;
import lombok.Setter;

public class AuthResponse {
    @Getter
    @Setter
    private final String accessToken;
    @Getter
    @Setter
    private final String tokenType = "Bearer";

    @Setter
    @Getter
    private final Long id;

    public AuthResponse(String accessToken, Long id) {
        this.accessToken = accessToken;
        this.id = id;
    }
}