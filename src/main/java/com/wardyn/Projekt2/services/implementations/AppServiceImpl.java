package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.App;
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
public class AppServiceImpl implements AppService {
    private List<App> appList;
    private final UserService userService;

    public AppServiceImpl(@Autowired List<App> appList, @Lazy UserService userService) {
        this.appList = appList;
        this.userService = userService;
    }

    @Override
    public List<App> getApps() {
        return this.appList;
    }

    @Override
    public App getAppById(Integer id) {
        if (this.appList != null) {
            for (App app : this.appList) {
                if (app.getId().equals(id)) {
                    return app;
                }
            }
        }
        return null;
    }

    @Override
    public List<App> getUserApps(List<Integer> appIds) {
        Predicate<App> byUserId = app -> appIds.contains(app.getId());
        return this.appList.stream().filter(byUserId).collect(Collectors.toList());
    }

    @Override
    public void addApp(App app) {
        app.setId(this.appList.get(this.appList.size() - 1).id + 1);

        this.appList.add(app);
        this.userService.updateAppInUsers(app.getUserList(), app.getId());
    }

    @Override
    public Boolean editApp(App app) {
        App appToEdit = getAppById(app.getId());

        if (appToEdit == null) {
            return false;
        }

        int index = this.appList.indexOf(appToEdit);

        this.appList.set(index, app);
        this.userService.updateAppInUsers(app.getUserList(), app.getId());

        return true;
    }

    @Override
    public void updateUserInApps(List<Integer> appIds, Integer userId) {
        for (App app : this.appList) {
            if (app.getUserList().contains(userId) && !appIds.contains(app.getId())) {
                List<Integer> users = app.getUserList();
                users.remove(userId);
                app.setUserList(users);
            } else if (!app.getUserList().contains(userId) && appIds.contains(app.getId())) {
                List<Integer> users = app.getUserList();
                users.add(userId);
                app.setUserList(users);
            }
        }
    }

    @Override
    public Boolean deleteApp(Integer appId) {
        App app = getAppById(appId);

        if (app == null) {
            return false;
        }

        this.appList.remove(app);
        userService.updateAppInUsers(new ArrayList<>(), appId);
        return true;
    }

    @Override
    public List<App> getAppsByUserId(Integer userId) {
        Predicate<App> byUserId = app -> app.getUserList().contains(userId);
        return this.appList.stream().filter(byUserId).collect(Collectors.toList());
    }

}
