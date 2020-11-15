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

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}