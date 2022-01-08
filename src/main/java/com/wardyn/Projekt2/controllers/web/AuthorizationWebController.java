package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class AuthorizationWebController {

    public AuthorizationWebController() {}

    @GetMapping("/login")
    public String loginView(Model model) {
        model.addAttribute("login", new Login());
        return  "user/loginForm";
    }

    @GetMapping("/register")
    public String registerView(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("action", "create");

        return "user/userForm";
    }

    @PostMapping("/login")
    public String login(Model model, BindingResult errors) {
        if (errors.hasErrors()) {
            model.addAttribute("error", "error!!!");

            return "user/loginForm";
        }

        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(Model model, BindingResult errors) {
        if (errors.hasErrors()) {
            model.addAttribute("error", "error!!!");
            model.addAttribute("action", "create");

            return "user/userForm";
        }

        return "redirect:/";
    }
}
