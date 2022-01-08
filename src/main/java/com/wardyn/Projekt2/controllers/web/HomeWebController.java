package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Random;

@Slf4j
@Controller
public class HomeWebController {
    private final AppService appService;
    private final UserService userService;
    private boolean loaded = false;

    @Autowired
    public HomeWebController(AppService appService, UserService userService) {
        this.appService = appService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        if (!loaded) {
            for (App app : appService.getApps()) {
                Random rand = new Random();
                int n = rand.nextInt(10);

                for (int i = 0; i < n; i++) {
                    Random rand2 = new Random();
                    int el = rand2.nextInt(200) + 1;
                    if (app.getUserList().contains(el)) {
                        i--;
                    } else {
                        List<Integer> newList = app.getUserList();
                        newList.add(el);
                        app.setUserList(newList);
                        User user = userService.getUserById(el);
                        List<Integer> newAppsList = user.getAppList();
                        newAppsList.add(app.getId());
                        user.setAppList(newAppsList);
                    }
                }
            }
            loaded = true;
        }
        return "home";
    }
}
