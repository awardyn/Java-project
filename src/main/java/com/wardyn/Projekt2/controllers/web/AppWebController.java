package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.Search;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.AuthorizationService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
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
    private final AuthorizationService authorizationService;

    @Autowired
    public AppWebController(AppService appService, UserService userService, AuthorizationService authorizationService) {
        this.appService = appService;
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/apps")
    public String getApps(Model model) {
        List<List<Object>> list = createListOfUsers();

        model.addAttribute("users", list);
        model.addAttribute("search", new Search());
        model.addAttribute("apps", appService.getApps());
        model.addAttribute("isAdmin", authorizationService.role().equals(Role.ADMIN));
        model.addAttribute("logged", authorizationService.role().equals(Role.ADMIN) || authorizationService.role().equals(Role.USER));

        return "app/apps";
    }

    @GetMapping("/apps/search")
    public String getAppsByUsername(Search search, Model model) {
        if(search.getSearchBy().equals(-1L)) {
            return "redirect:/apps";
        }

        List<List<Object>> list = createListOfUsers();

        User userSearchBy = userService.getUserById(search.getSearchBy());

        model.addAttribute("users", list);
        model.addAttribute("search", new Search(search.getSearchBy()));
        model.addAttribute("apps", appService.getAppsByUserId(userSearchBy));
        model.addAttribute("isAdmin", authorizationService.role().equals(Role.ADMIN));
        model.addAttribute("logged", authorizationService.role().equals(Role.ADMIN) || authorizationService.role().equals(Role.USER));

        return "app/apps";
    }

    @GetMapping("/apps/{id}")
    public String getAppById(@PathVariable Long id, Model model) {
        App app = appService.getAppById(id);
        if (app == null) {
            model.addAttribute("error", "There is no app with given id");
            return "app/app";
        }

        model.addAttribute("isAdmin", authorizationService.role().equals(Role.ADMIN));
        model.addAttribute("app", app);
        model.addAttribute("users", app.getUserList());

        return "app/app";
    }

    @GetMapping("/apps/create")
    public String appCreate(Model model) {
        boolean isAdmin =  authorizationService.role().equals(Role.ADMIN);

        if (!isAdmin) {
            return "redirect:/apps";
        }

        model.addAttribute("app", new App());
        model.addAttribute("action", "create");

        return "app/appForm";
    }

    @GetMapping("/apps/{id}/edit")
    public String appEdit(@PathVariable Long id, Model model) {
        boolean isAdmin =  authorizationService.role().equals(Role.ADMIN);

        if (!isAdmin) {
            return "redirect:/apps";
        }

        App app = appService.getAppById(id);
        if (app == null) {
            model.addAttribute("error", "There is no app with given id");
            return "app/appForm";
        }

        model.addAttribute("app", app);
        model.addAttribute("action", "edit");

        return "app/appForm";
    }

    @PostMapping("/apps/create")
    public String createApp(@Valid App app, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("action", "create");
            return "app/appForm";
        }

        List<String> domains = appService.getApps().stream().map(App::getDomain).collect(Collectors.toList());

        if (domains.contains(app.getDomain())) {
            ObjectError error = new ObjectError("domain", "Domain is not unique");
            errors.addError(error);
            model.addAttribute("action", "create");
            return "app/appForm";
        }

        appService.addApp(app);

        return "redirect:/apps";
    }

    @PostMapping("/apps/{id}/edit")
    public String editApp(@Valid App app, BindingResult errors, RedirectAttributes redirectAttributes, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("action", "edit");
            return "app/appForm";
        }

        List<String> domains = appService.getApps().stream().filter(a -> !Objects.equals(a.getId(), app.getId())).map(App::getDomain).collect(Collectors.toList());

        if (domains.contains(app.getDomain())) {
            ObjectError error = new ObjectError("domain", "Domain is not unique");
            errors.addError(error);
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
    public String deleteApp(@PathVariable Long id, RedirectAttributes redirectAttributes) {
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
        all.add(-1L);
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
