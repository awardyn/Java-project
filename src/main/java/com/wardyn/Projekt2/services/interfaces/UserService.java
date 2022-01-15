package com.wardyn.Projekt2.services.interfaces;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();
    Optional<User> getUserById(Long id);
    void addUser(User user);
    Boolean editUser(User user);
    Boolean deleteUser(Long userId);
    List<User> getUsersByAppId(App app);
}
