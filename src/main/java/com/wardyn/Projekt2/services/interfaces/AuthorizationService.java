package com.wardyn.Projekt2.services.interfaces;

import com.wardyn.Projekt2.domains.Login;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;

public interface AuthorizationService {
    Role role();
    User getLoggedUser();
    boolean login(Login login);
    void register(User user);
}
