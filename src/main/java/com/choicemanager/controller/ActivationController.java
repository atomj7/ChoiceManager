package com.choicemanager.controller;

import com.choicemanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ActivationController {

    private final UserService userService;

    ActivationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
        return userService.activateUser(code) ?
                ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(Map.of("message",
                                "User successfully activated"))
                : ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message",
                        "Activation code is not found!"));
    }

}
