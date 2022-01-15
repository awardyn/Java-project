package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class UploadWebController {
    private final AppService appService;
    private final UserService userService;

    public UploadWebController(AppService appService, UserService userService) {
        this.appService = appService;
        this.userService = userService;
    }

    @PostMapping("/export/apps")
    public void exportApps(HttpServletResponse response) throws IOException {
        List<String> apps = createAppCsvData();
        InputStream is = new ByteArrayInputStream(String.join("\n", apps).getBytes());
        IOUtils.copy(is, response.getOutputStream());
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"apps.csv\"");
        response.flushBuffer();
    }

    @PostMapping("/export/users")
    public void exportUsers(HttpServletResponse response) throws IOException {
        List<String> users = createUserCsvData();
        InputStream is = new ByteArrayInputStream(String.join("\n", users).getBytes());
        IOUtils.copy(is, response.getOutputStream());
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"users.csv\"");
        response.flushBuffer();
    }

    private List<String> createAppCsvData() {
        List<String> list = new ArrayList<>();
        String header = "id,name,domain";
        list.add(header);

        for (App app : this.appService.getApps()) {
            String record = app.getId() + "," + app.getName() + "," + app.getDomain();
            list.add(record);
        }
        return list;
    }

    private List<String> createUserCsvData() {
        List<String> list = new ArrayList<>();
        String header = "id,first_name,last_name,email,country,username,password";
        list.add(header);

        for (User user : this.userService.getUsers()) {
            String record = user.getId() + "," + user.getFirstName() + "," + user.getLastName() + "," + user.getEmail() + "," + user.getCountry() + "," + user.getUsername() + "," + user.getUserPassword();
            list.add(record);
        }
        return list;
    }
}
