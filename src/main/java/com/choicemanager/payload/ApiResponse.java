package com.choicemanager.payload;

import lombok.Getter;
import lombok.Setter;

public class ApiResponse {
    @Getter
    @Setter
    private boolean success;
    @Getter
    @Setter
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}