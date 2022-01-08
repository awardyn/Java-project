package com.wardyn.Projekt2.services.interfaces;

import com.wardyn.Projekt2.domains.App;

import java.util.List;

public interface AppService {
    List<App> getApps();
    App getAppById(Integer id);
    void addApp(App app);
    Boolean editApp(App app);
    List<App> getUserApps(List<Integer> appIds);
    void updateUserInApps(List<Integer> appIds, Integer userId);
    Boolean deleteApp(Integer appId);
    List<App> getAppsByUserId(Integer userId);
}
