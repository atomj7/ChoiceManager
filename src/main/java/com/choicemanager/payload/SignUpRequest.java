package com.choicemanager.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SignUpRequest {
    @NotBlank
    @Getter
    @Setter
    private String login;
    @Getter
    @Setter
    @NotBlank
    @Email
    private String email;
    @Getter
    @Setter
    @NotBlank
    private String password;

    @Getter
    @Setter
    @NotBlank
    private String passwordConfirm;

    @Getter
    @Setter
    @NotBlank
    private String name;

    @Getter
    @Setter
    @NotBlank
    private String surname;

}