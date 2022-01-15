package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.repositories.AppRepository;
import com.wardyn.Projekt2.services.interfaces.AppService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AppServiceImpl implements AppService {
    final AppRepository appRepository;

    public AppServiceImpl(AppRepository appRepository) {
        this.appRepository = appRepository;
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

}
