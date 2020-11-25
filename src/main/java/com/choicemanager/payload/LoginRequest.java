package com.choicemanager.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @Getter
    @Setter
    private String usernameOrEmail;

    @NotBlank
    @Getter
    @Setter
    private String password;
}