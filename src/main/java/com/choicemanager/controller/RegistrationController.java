package com.choicemanager.controller;

import com.choicemanager.domain.User;
import com.choicemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<Object> userRegistration(@RequestBody @Valid User userData,
                                                   BindingResult bindingResult) {
        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
        if (userData == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("data is null" + errorsMap);
        }
        if (userData.getPassword() != null && !userData.getPassword().equals(userData.getPasswordConfirmation())) {
            errorsMap.put("passwordError", "Password are different!");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(errorsMap);
        }
        System.out.println(userData);
        if (bindingResult.hasErrors()) {
            System.out.println(errorsMap.toString());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(errorsMap);
        }
        if (!userService.addUser(userData)) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(
                    Map.of("message", "user already exist"));
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}




