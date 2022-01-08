package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.Search;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class AppWebController {
    private final AppService appService;
    private final UserService userService;

    @Autowired
    public AppWebController(AppService appService, UserService userService) {
        this.appService = appService;
        this.userService = userService;
    }

    @GetMapping("/apps")
    public String getApps(Model model) {
        List<List<Object>> list = createListOfUsers();

        model.addAttribute("users", list);
        model.addAttribute("search", new Search());
        model.addAttribute("apps", appService.getApps());

        return "app/apps";
    }

    @GetMapping("/apps/search")
    public String getAppsByUsername(Search search, Model model) {
        if(search.getSearchBy().equals(-1)) {
            return "redirect:/apps";
        }

        List<List<Object>> list = createListOfUsers();

        model.addAttribute("users", list);
        model.addAttribute("search", new Search(search.getSearchBy()));
        model.addAttribute("apps", appService.getAppsByUserId(search.getSearchBy()));

        return "app/apps";
    }

    @GetMapping("/apps/{id}")
    public String getAppById(@PathVariable Integer id, Model model) {
        App app = appService.getAppById(id);
        if (app == null) {
            model.addAttribute("error", "There is no app with given id");
            return "app/app";
        }

        model.addAttribute("app", app);
        model.addAttribute("users", userService.getAppUsers(app.getUserList()));

        return "app/app";
    }

    @GetMapping("/apps/create")
    public String appCreate(Model model) {
        model.addAttribute("app", new App());
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("action", "create");

        return "app/appForm";
    }

    @GetMapping("/apps/{id}/edit")
    public String appEdit(@PathVariable Integer id, Model model) {
        App app = appService.getAppById(id);
        if (app == null) {
            model.addAttribute("error", "There is no app with given id");
            return "app/appForm";
        }

        model.addAttribute("app", app);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("action", "edit");

        return "app/appForm";
    }

    @PostMapping("/apps/create")
    public String createApp(@Valid App app, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("users", userService.getUsers());
            model.addAttribute("action", "create");
            return "app/appForm";
        }

        List<String> domains = appService.getApps().stream().map(App::getDomain).collect(Collectors.toList());

        if (domains.contains(app.getDomain())) {
            ObjectError error = new ObjectError("domain", "Domain is not unique");
            errors.addError(error);
            model.addAttribute("users", userService.getUsers());
            model.addAttribute("action", "create");
            return "app/appForm";
        }

        appService.addApp(app);

        return "redirect:/apps";
    }

    @PostMapping("/apps/{id}/edit")
    public String editApp(@Valid App app, BindingResult errors, RedirectAttributes redirectAttributes, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("users", userService.getUsers());
            model.addAttribute("action", "edit");
            return "app/appForm";
        }

        List<String> domains = appService.getApps().stream().filter(a -> !Objects.equals(a.getId(), app.getId())).map(App::getDomain).collect(Collectors.toList());

        if (domains.contains(app.getDomain())) {
            ObjectError error = new ObjectError("domain", "Domain is not unique");
            errors.addError(error);
            model.addAttribute("users", userService.getUsers());
            model.addAttribute("action", "edit");
            return "app/appForm";
        }

        Boolean edited = appService.editApp(app);

        if (edited.equals(false)) {
            redirectAttributes.addFlashAttribute("error", "There is no app with given id");
            return "redirect:/apps/" + app.getId() + "/edit";
        }

        return "redirect:/apps/" + app.getId();
    }

    @DeleteMapping("/apps/delete/{id}")
    public String deleteApp(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Boolean deleted = appService.deleteApp(id);
        if (deleted.equals(false)) {
            redirectAttributes.addFlashAttribute("error", "There is no app with given id to delete");
            return "redirect:/apps";
        }
        return "redirect:/apps";
    }

    private List<List<Object>> createListOfUsers() {
        List<List<Object>> list = new ArrayList<>();
        List<Object> all = new ArrayList<>();
        all.add(-1);
        all.add("all");

        list.add(all);

        List<User> userList = userService.getUsers();

        for (User user : userList) {
            List<Object> element = new ArrayList<>();
            element.add(user.getId());
            element.add(user.getUsername());
            list.add(element);
        }

        return list;
    }
}
