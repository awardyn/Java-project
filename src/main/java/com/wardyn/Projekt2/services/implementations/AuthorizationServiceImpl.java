package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.repositories.UserRepository;
import com.wardyn.Projekt2.services.interfaces.AuthorizationService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private Role role = Role.GUEST;
    private User loggedUser;
    final UserRepository userRepository;

    public AuthorizationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        System.out.println(userRepository.findAll());
        User user = userRepository.findUserByUsernameAndUserPassword(login.getUsername(), login.getPassword());
        if (user == null) {
            return false;
        }
        this.role = Role.USER;
        this.loggedUser = user;
        return true;
    }

    @Override
    public void register(User user) {
        userRepository.save(user);
    }
}
