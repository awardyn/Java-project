package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.repositories.AppRepository;
import com.wardyn.Projekt2.repositories.UserRepository;
import com.wardyn.Projekt2.services.interfaces.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AppServiceImpl implements AppService {
    final AppRepository appRepository;
    final UserRepository userRepository;
    private final List<App> apps;
    private final List<User> users;

    public AppServiceImpl(AppRepository appRepository, UserRepository userRepository, @Autowired List<App> appList, @Autowired List<User> userList) {
        this.appRepository = appRepository;
        this.userRepository = userRepository;
        this.apps = appList;
        this.users = userList;
    }

    @Override
    public List<App> getApps() {
        List<App> appList = new ArrayList<>();
        this.appRepository.findAll().forEach(appList::add);
        return appList;
    }

    @Override
    public Optional<App> getAppById(Long id) {
        return this.appRepository.findById(id);
    }

    @Override
    public void addApp(App app) {
        this.appRepository.save(app);
    }

    @Override
    public Boolean editApp(App app) {
        Optional<App> appToUpdate = getAppById(app.id);

        if (!appToUpdate.isPresent()) {
            return false;
        }

        app.setId(appToUpdate.get().getId());

        this.appRepository.save(app);

        return true;
    }


    @Override
    public Boolean deleteApp(Long appId) {
        Optional<App> app = this.appRepository.findById(appId);

        if (!app.isPresent()) {
            return false;
        }

        this.appRepository.delete(app.get());

        return true;
    }

    @Override
    public List<App> getAppsByUserId(User user) {
        return this.appRepository.findAllByUserListIsContaining(user);
    }

    @Override
    public void learning() {

        appRepository.saveAll(apps);

        for (User user : users) {
            user.setRole(Role.USER);
            if (user.getUserPassword().length() + 3 >= 30) {
                user.setUserPassword("Qwer1234!");
            } else {
                user.setUserPassword(user.getUserPassword() + "1Q!");
            }
        }

        for (App app :apps) {
            Random rand = new Random();
            int n = rand.nextInt(10);
            List<Integer> userIds = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                Random rand2 = new Random();
                int el = rand2.nextInt(200) + 1;
                if (userIds.contains(el)) {
                    i--;
                } else {
                    userIds.add(el);
                }
            }
            for (Integer id : userIds) {
                for (User user : users) {
                    if (user.getId().equals(Long.parseLong(String.valueOf(id)))) {
                        List<App> appList = user.getAppList();
                        appList.add(app);
                        user.setAppList(appList);
                    }
                }
            }
        }

        User admin = new User("admin", "admin", "admin@project.com", "Polska", "admin", "0uC32wVsKA1Q!", Role.ADMIN);

        users.add(admin);

        userRepository.saveAll(users);
    }

}
