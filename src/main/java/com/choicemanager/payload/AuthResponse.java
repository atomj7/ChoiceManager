package com.choicemanager.payload;

import lombok.Getter;
import lombok.Setter;

public class AuthResponse {
    @Getter
    @Setter
    private String accessToken;
    @Getter
    @Setter
    private String tokenType = "Bearer";

    @Setter
    @Getter
    private Long id;

    public AuthResponse(String accessToken, Long id) {
        this.accessToken = accessToken;
        this.id = id;
    }
}