package com.wardyn.Projekt2.controllers.api;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class UserApiController {
    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users/{id}")
    User userById(@PathVariable Long id) throws Exception {
        User user = this.userService.getUserById(id);

        if (user == null) {
            throw new Exception("There is no user with given id");
        }

        return user;
    }
}
