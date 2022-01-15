package com.wardyn.Projekt2.services.interfaces;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;

import java.util.Optional;

public interface AuthorizationService {
    Optional<User> login(Login login);
    void register(User user);
}
