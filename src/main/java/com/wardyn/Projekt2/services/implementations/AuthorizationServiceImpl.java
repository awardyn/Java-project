package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.repositories.UserRepository;
import com.wardyn.Projekt2.services.interfaces.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    final UserRepository userRepository;

    public AuthorizationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> login(Login login) {
        return userRepository.findUserByUsernameAndUserPassword(login.getUsername(), login.getPassword());
    }

    @Override
    public void register(User user) {
        userRepository.save(user);
    }
}
