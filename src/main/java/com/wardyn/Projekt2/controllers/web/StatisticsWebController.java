package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class StatisticsWebController {
    private final UserService userService;
    private final AppService appService;

    @Autowired
    public StatisticsWebController(UserService userService, AppService appService) {
        this.userService = userService;
        this.appService = appService;
    }

    @GetMapping("/statistics/{id}")
    public String getStatistics(@PathVariable Integer id, Model model) {
        if (id > 6 || id < 1) {
            return "redirect:/";
        }
        List<Object> values = new ArrayList<>();
        switch (id) {
            case 1:
                values = firstStat();
                break;
            case 2:
                values = secondStat();
                break;
            case 3:
                values = thirdStat();
                break;
            case 4:
                values = fourthStat();
                break;
            case 5:
                values = fifthStat();
                break;
            case 6:
                values = sixthStat();
                break;
            default:
                break;
        }
        model.addAttribute("title", values.get(0));
        model.addAttribute("firstValue", values.get(1));
        model.addAttribute("secondValue", values.get(2));
        model.addAttribute("firstHeight", values.get(3));
        model.addAttribute("secondHeight", values.get(4));
        return "statistics/statistics";
    }

    private List<Object> firstStat() {
        List<Object> values = new ArrayList<>();
        values.add("More than 6 letters in country name");
        values.add("More than");
        values.add("Less than");
        long firstValue = userService.getUsers().stream().filter(u -> u.getCountry().length() > 6).count() * 100 / userService.getUsers().size();
        values.add(firstValue);
        values.add(100 - firstValue);
        return values;
    }

    private List<Object> secondStat() {
        List<Object> values = new ArrayList<>();
        values.add("Domains with .com");
        values.add(".com");
        values.add("Not .com");
        long firstValue = appService.getApps().stream().filter(a -> a.getDomain().contains(".com")).count() * 100 / appService.getApps().size();
        values.add(firstValue);
        values.add(100 - firstValue);
        return values;
    }

    private List<Object> thirdStat() {
        List<Object> values = new ArrayList<>();
        values.add("App has more than 3 users");
        values.add("More than 3 users");
        values.add("Less than 3 users");
        long firstValue = appService.getApps().stream().filter(a -> a.getUserList().size() > 3).count() * 100 / appService.getApps().size();
        values.add(firstValue);
        values.add(100 - firstValue);
        return values;
    }

    private List<Object> fourthStat() {
        List<Object> values = new ArrayList<>();
        values.add("User first name with starting A letter");
        values.add("Starts with A");
        values.add("Not starts with A");
        long firstValue = userService.getUsers().stream().filter(u -> u.getFirstName().startsWith("A")).count() * 100 / userService.getUsers().size();
        values.add(firstValue);
        values.add(100 - firstValue);
        return values;
    }

    private List<Object> fifthStat() {
        List<Object> values = new ArrayList<>();
        values.add("User has more than 3 apps");
        values.add("More than 3 apps");
        values.add("Less than 3 apps");
        long firstValue = userService.getUsers().stream().filter(u -> u.getAppList().size() > 3).count() * 100 / userService.getUsers().size();
        System.out.println(firstValue);
        values.add(firstValue);
        values.add(100 - firstValue);
        return values;
    }

    private List<Object> sixthStat() {
        List<Object> values = new ArrayList<>();
        values.add("Users with .com in email");
        values.add(".com");
        values.add("Not .com");
        long firstValue = userService.getUsers().stream().filter(u -> u.getEmail().contains(".com")).count() * 100 / userService.getUsers().size();
        values.add(firstValue);
        values.add(100 - firstValue);
        return values;
    }
}
