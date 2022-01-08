package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final List<User> userList;
    private final AppService appService;

    public UserServiceImpl(@Autowired List<User> userList, @Lazy AppService appService) {
        this.userList = userList;
        this.appService = appService;
    }

    @Override
    public List<User> getUsers() {
        return this.userList;
    }

    @Override
    public User getUserById(Integer id) {
        if (this.userList != null) {
            for (User user : this.userList) {
                if (user.getId().equals(id)) {
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public List<User> getAppUsers(List<Integer> userIds) {
        Predicate<User> byAppId = user -> userIds.contains(user.getId());
        return this.userList.stream().filter(byAppId).collect(Collectors.toList());
    }

    @Override
    public void updateAppInUsers(List<Integer> userIds, Integer appId) {
        for (User user : this.userList) {
            if (user.getAppList().contains(appId) && !userIds.contains(user.getId())) {
                List<Integer> apps = user.getAppList();
                apps.remove(appId);
                user.setAppList(apps);
            } else if (!user.getAppList().contains(appId) && userIds.contains(user.getId())) {
                List<Integer> apps = user.getAppList();
                apps.add(appId);
                user.setAppList(apps);
            }
        }
    }

    @Override
    public void addUser(User user) {
        user.setId(this.userList.get(this.userList.size() - 1).id + 1);

        this.userList.add(user);
        this.appService.updateUserInApps(user.getAppList(), user.getId());
    }

    @Override
    public Boolean editUser(User user) {
        User userToEdit = getUserById(user.getId());

        if (userToEdit == null) {
            return false;
        }

        int index = this.userList.indexOf(userToEdit);

        this.userList.set(index, user);
        this.appService.updateUserInApps(user.getAppList(), user.getId());

        return true;
    }

    @Override
    public Boolean deleteUser(Integer userId) {
        User user = getUserById(userId);

        if (user == null) {
            return false;
        }

        this.userList.remove(user);
        appService.updateUserInApps(new ArrayList<>(), userId);
        return true;
    }

    @Override
    public List<User> getUsersByAppId(Integer appId) {
        Predicate<User> byAppId = user -> user.getAppList().contains(appId);
        return this.userList.stream().filter(byAppId).collect(Collectors.toList());
    }
}
