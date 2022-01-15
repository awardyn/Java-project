package com.wardyn.Projekt2.controllers.api;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.services.interfaces.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class AppApiController {
    private final AppService appService;

    @Autowired
    public AppApiController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/api/apps/{id}")
    HashMap<String, HashMap<String, Object>> appById(@PathVariable Long id) throws Exception {
        App app = this.appService.getAppById(id);

        if (app == null) {
            throw new Exception("There is no app with given id");
        }

        HashMap<String, HashMap<String, Object>> map = new HashMap<>();
        HashMap<String,Object> insideMap = new HashMap<>();
        insideMap.put("nazwa_domeny", app.getDomain());
        insideMap.put("ilosc_uzytkownikow", app.getUserList().size());
        map.put(app.getName(), insideMap);

        return map;
    }
}
