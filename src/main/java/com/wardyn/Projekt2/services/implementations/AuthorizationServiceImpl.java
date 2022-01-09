package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.services.interfaces.AuthorizationService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private Role role = Role.GUEST; //TODO manage roles
    private User loggedUser;
    private final UserService userService;

    public AuthorizationServiceImpl(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public Role role() {
        return role;
    }

    @Override
    public User getLoggedUser() {
        return loggedUser;
    }

    @Override
    public boolean login(Login login) {
        User user = userService.getUserById(1);
        if (user == null) {
            return false;
        }

        this.loggedUser = user;
        return true;
    }

    @Override
    public void register(User user) {
        userService.addUser(user);
    }
}
