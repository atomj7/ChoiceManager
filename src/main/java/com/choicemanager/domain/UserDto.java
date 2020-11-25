package com.choicemanager.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class UserDto {
    @Id
    private Long id;
    private String login;
    @NotBlank(message = "Email can not be empty")
    @Email(message = "Please provide a valid email id")
    private String email;
    @NotBlank(message = "Name can not be empty")
    private String name;
    @NotBlank(message = "Surname can not be empty")
    private String surname;
    private String imageUrl;
    private boolean active;

    public UserDto() {
    }
}