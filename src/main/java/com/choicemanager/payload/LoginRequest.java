package com.choicemanager.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @Email
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String login;

    @NotBlank
    @Getter
    @Setter
    private String password;
}