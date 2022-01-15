package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.services.interfaces.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class AuthorizationWebController {

    private final AuthorizationService authorizationService;

    public AuthorizationWebController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/login")
    public String loginView(Model model) {
        if (!authorizationService.role().equals(Role.GUEST)) {
            return "redirect:/";
        }
        model.addAttribute("login", new Login());
        return  "user/loginForm";
    }

    @GetMapping("/register")
    public String registerView(Model model) {
        if (!authorizationService.role().equals(Role.GUEST)) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        model.addAttribute("action", "create");

        return "user/userForm";
    }

    @PostMapping("/login")
    public String login(Login login, Model model, BindingResult errors) {
        boolean loggedIn = authorizationService.login(login);

        if (!loggedIn) {
            model.addAttribute("error", "Username or password are incorrect, try again");
            model.addAttribute("login", new Login(login));
            return "user/loginForm";
        }

        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(@Valid User user, Model model, BindingResult errors) {
        if (errors.hasErrors()) {
            model.addAttribute("action", "create");
            return "user/userForm";
        }

        authorizationService.register(user);

        return "redirect:/";
    }
}
