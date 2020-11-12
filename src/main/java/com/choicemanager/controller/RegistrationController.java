package com.choicemanager.controller;

import com.choicemanager.domain.User;
import com.choicemanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class RegistrationController {

    private final UserService userService;

    RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<?> userRegistration(@RequestBody @Valid User userData,
                                                   BindingResult bindingResult) {
        if (userService.errorValidationProcessing(bindingResult, userData) != null) {
            return userService.errorValidationProcessing(bindingResult, userData);
        }
        if (!userService.addUser(userData)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    Map.of("message",  "Already exist"));
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}




