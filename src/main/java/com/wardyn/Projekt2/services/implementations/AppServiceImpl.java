package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.repositories.AppRepository;
import com.wardyn.Projekt2.repositories.UserRepository;
import com.wardyn.Projekt2.services.interfaces.AppService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppServiceImpl implements AppService {
    final AppRepository appRepository;
    final UserRepository userRepository;

    public AppServiceImpl(AppRepository appRepository, UserRepository userRepository) {
        this.appRepository = appRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<App> getApps() {
        List<App> appList = new ArrayList<>();
        this.appRepository.findAll().forEach(appList::add);
        return appList;
    }

    @Override
    public App getAppById(Long id) {
        return this.appRepository.findAppById(id);
    }

    @Override
    public void addApp(App app) {
        this.appRepository.save(app);
    }

    @Override
    public Boolean editApp(App app) {
        App appToUpdate = getAppById(app.id);

        if (appToUpdate == null) {
            return false;
        }

        app.setId(appToUpdate.getId());

        this.appRepository.save(app);

        return true;
    }


    @Override
    public Boolean deleteApp(Long appId) {
        App app = this.appRepository.findAppById(appId);

        if (app == null) {
            return false;
        }
        this.appRepository.delete(app);

        return true;
    }

    @Override
    public List<App> getAppsByUserId(User user) {
        return this.appRepository.findAllByUserListIsContaining(user);
    }

    @Override
    public void learning() {
        App app1 = new App("name1", "domain1.com");
        App app2 = new App("name2", "domain2.com");

        List<App> appList = new ArrayList<>();
        appList.add(app1);
        appList.add(app2);

        appRepository.save(app1);
        appRepository.save(app2);

        User user = new User("firstName", "secondName", "email@gmail.com", "Poland", "MelkorW", "0uC32wVsKA!", Role.USER);
        user.setAppList(appList);

        User userSaved = userRepository.save(user);
    }

}
