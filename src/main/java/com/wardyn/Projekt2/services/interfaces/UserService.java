package com.wardyn.Projekt2.services.interfaces;

import com.wardyn.Projekt2.domains.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User getUserById(Integer id);
    List<User> getAppUsers(List<Integer> userIds);
    void updateAppInUsers(List<Integer> userIds, Integer appId);
    void addUser(User user);
    Boolean editUser(User user);
    Boolean deleteUser(Integer userId);
    List<User> getUsersByAppId(Integer appId);
}
