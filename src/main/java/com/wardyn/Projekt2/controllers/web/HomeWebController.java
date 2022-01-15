package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
public class HomeWebController {
    private final UserService userService;

    public HomeWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, @CookieValue(value = "id", defaultValue = "-1") String id) {
        Long parsedId = Long.parseLong(id);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (!loggedUser.isPresent()) {
            model.addAttribute("isAdmin", false);
            model.addAttribute("isUser", false);
            model.addAttribute("isGuest", true);
        } else {
            User user = loggedUser.get();

            boolean isAdmin = user.getRole().equals(Role.ADMIN);
            boolean isUser = user.getRole().equals(Role.USER);

            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("isUser", isUser);
            model.addAttribute("isGuest", false);

            model.addAttribute("user", user);
            model.addAttribute("apps", user.getAppList());
        }

        return "home";
    }
}
