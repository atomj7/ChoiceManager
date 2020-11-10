package com.choicemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String root() {
        return "root";
    }

    @RequestMapping(value = "/home")
    public RedirectView homeRedirectToRoot() {
        return new RedirectView("/");
    }

}

