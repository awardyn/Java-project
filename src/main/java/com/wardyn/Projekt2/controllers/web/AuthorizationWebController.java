package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.services.interfaces.AuthorizationService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class AuthorizationWebController {

    private final AuthorizationService authorizationService;
    private final UserService userService;


    public AuthorizationWebController(AuthorizationService authorizationService, UserService userService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginView(Model model, @CookieValue(value = "id", defaultValue = "-1") String id) {
        Long parsedId = Long.parseLong(id);
        List<User> userList = userService.getUsers();
        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (loggedUser.isPresent()) {
            return "redirect:/";
        }

        model.addAttribute("login", new Login());
        return  "user/loginForm";
    }

    @GetMapping("/register")
    public String registerView(Model model, @CookieValue(value = "id", defaultValue = "-1") String id) {
        Long parsedId = Long.parseLong(id);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (loggedUser.isPresent()) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        model.addAttribute("action", "create");

        return "user/userForm";
    }

    @PostMapping("/login")
    public String login(Login login, Model model, HttpServletResponse response) {

        Optional<User> user = authorizationService.login(login);

        if (!user.isPresent()) {
            model.addAttribute("error", "Username or password are incorrect, try again");
            model.addAttribute("login", new Login(login));
            return "user/loginForm";
        }
        user.ifPresent(userLoggedIn -> {
            Cookie cookie = new Cookie("id", userLoggedIn.getId().toString());
            response.addCookie(cookie);
        });

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(Login login, Model model, HttpServletResponse response) {
        Optional<User> user = authorizationService.login(login);

        Cookie cookie = new Cookie("id", "-1");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("action", "create");
            return "user/userForm";
        }

        List<String> usernames = userService.getUsers().stream().map(User::getUsername).collect(Collectors.toList());

        if (usernames.contains(user.getUsername())) {
            ObjectError error = new ObjectError("domain", "Username is not unique");
            errors.addError(error);
            model.addAttribute("action", "create");
            return "user/userForm";
        }

        user.setRole(Role.USER);
        authorizationService.register(user);

        return "redirect:/";
    }
}
