package com.choicemanager.controller;

import com.choicemanager.domain.User;
import com.choicemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping(value = "/registration")
    public @ResponseBody String userRegistration(@RequestBody @Valid User userData,
                                                 BindingResult bindingResult,
                                                 Model model){
        if(userData == null) {
            model.addAttribute("UserData", "is Empty");
            return "registration";
        }
        if (userData.getPassword() != null && !userData.getPassword().equals(userData.getPasswordConfirmation())) {
            model.addAttribute("passwordError", "Password are different!");
        }
        System.out.println(userData);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("userData", userData);
            System.out.println(model.asMap().toString());
            return "registration";
        }
        if (!userService.addUser(userData)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }
}




