package com.choicemanager.controller;

import com.choicemanager.domain.User;
import com.choicemanager.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<String> userRegistration(@RequestBody @Valid User userData,
                                                       BindingResult bindingResult){
        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
        if(userData == null) {
            return new ResponseEntity<>("data is null" + errorsMap, HttpStatus.BAD_REQUEST);
        }
        if (userData.getPassword() != null && !userData.getPassword().equals(userData.getPasswordConfirmation())) {
            errorsMap.put("passwordError", "Password are different!");
            return new ResponseEntity<>(errorsMap.toString(), HttpStatus.NOT_ACCEPTABLE);
        }
        System.out.println(userData);
        if (bindingResult.hasErrors()) {
            System.out.println(errorsMap.toString());
            return new ResponseEntity<>(errorsMap.toString(), HttpStatus.NOT_ACCEPTABLE);
        }
        if (!userService.addUser(userData)) {
            return  new ResponseEntity<>("user already exist", HttpStatus.I_AM_A_TEAPOT);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}




