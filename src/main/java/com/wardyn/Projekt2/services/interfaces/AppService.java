package com.wardyn.Projekt2.services.interfaces;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;

import java.util.List;

public interface AppService {
    List<App> getApps();
    App getAppById(Long id);
    void addApp(App app);
    Boolean editApp(App app);
    Boolean deleteApp(Long appId);
    List<App> getAppsByUserId(User user);
}
