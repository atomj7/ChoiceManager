package com.choicemanager.payload;

import lombok.Getter;
import lombok.Setter;

public class AuthResponse {
    @Getter
    @Setter
    private String accessToken;

    @Getter
    private final String tokenType = "Bearer";

    @Getter
    @Setter
    private boolean isTested;

    public AuthResponse(String accessToken, boolean isTested) {
        this.accessToken = accessToken;
        this.isTested = isTested;
    }
}