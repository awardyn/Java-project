package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.Search;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.services.interfaces.AppService;
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
import java.util.Optional;
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
    public String getApps(Model model, @CookieValue(value = "id", defaultValue = "-1") String id) {
        List<List<Object>> list = createListOfUsers();

        Long parsedId = Long.parseLong(id);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (!loggedUser.isPresent()) {
            model.addAttribute("isAdmin", false);
            model.addAttribute("logged", false);
            model.addAttribute("loggedUser", null);
        } else {
            User user = loggedUser.get();
            model.addAttribute("isAdmin", user.getRole().equals(Role.ADMIN));
            model.addAttribute("logged", true);
            model.addAttribute("loggedUser", user);
        }

        model.addAttribute("users", list);
        model.addAttribute("search", new Search());
        model.addAttribute("apps", appService.getApps());


        return "app/apps";
    }

    @GetMapping("/apps/search")
    public String getAppsByUsername(Search search, Model model, @CookieValue(value = "id", defaultValue = "-1") String id) {
        if(search.getSearchBy().equals(-1L)) {
            return "redirect:/apps";
        }

        List<List<Object>> list = createListOfUsers();

        User userSearchBy = userService.getUserById(search.getSearchBy()).get();

        Long parsedId = Long.parseLong(id);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (!loggedUser.isPresent()) {
            model.addAttribute("isAdmin", false);
            model.addAttribute("logged", false);
            model.addAttribute("loggedUser", null);
        } else {
            User user = loggedUser.get();
            model.addAttribute("isAdmin", user.getRole().equals(Role.ADMIN));
            model.addAttribute("logged", true);
            model.addAttribute("loggedUser", user);
        }

        model.addAttribute("users", list);
        model.addAttribute("search", new Search(search.getSearchBy()));
        model.addAttribute("apps", appService.getAppsByUserId(userSearchBy));

        return "app/apps";
    }

    @GetMapping("/apps/{id}")
    public String getAppById(@PathVariable Long id, Model model, @CookieValue(value = "id", defaultValue = "-1") String cookieId) {
        Optional<App> app = appService.getAppById(id);
        if (!app.isPresent()) {
            model.addAttribute("error", "There is no app with given id");
            return "app/app";
        }

        Long parsedId = Long.parseLong(cookieId);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (loggedUser.isPresent()) {
            User user = loggedUser.get();
            model.addAttribute("isAdmin", user.getRole().equals(Role.ADMIN));

        } else {
            model.addAttribute("isAdmin", false);
        }

        model.addAttribute("app", app.get());
        model.addAttribute("users", app.get().getUserList());

        return "app/app";
    }

    @GetMapping("/apps/{id}/assign")
    public String assignApp(@PathVariable Long id, Model model, @CookieValue(value = "id", defaultValue = "-1") String cookieId) {
        Optional<App> app = appService.getAppById(id);
        if (!app.isPresent()) {
            model.addAttribute("error", "There is no app with given id");
            return "app/apps";
        }

        Long parsedId = Long.parseLong(cookieId);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (loggedUser.isPresent()) {
            User user = loggedUser.get();
            if (user.getRole().equals(Role.ADMIN)) {
                List<List<Object>> list = createListOfUsers();
                model.addAttribute("error", "Admin cannot assign apps to himself");
                model.addAttribute("isAdmin", user.getRole().equals(Role.ADMIN));
                model.addAttribute("logged", true);
                model.addAttribute("loggedUser", user);
                model.addAttribute("users", list);
                model.addAttribute("search", new Search());
                model.addAttribute("apps", appService.getApps());
                return "app/apps";
            }
            List<App> userAppList = user.getAppList();
            if (userAppList.contains(app.get())) {
                List<List<Object>> list = createListOfUsers();
                model.addAttribute("error", "App is already assigned to user");
                model.addAttribute("isAdmin", user.getRole().equals(Role.ADMIN));
                model.addAttribute("logged", true);
                model.addAttribute("loggedUser", user);
                model.addAttribute("users", list);
                model.addAttribute("search", new Search());
                model.addAttribute("apps", appService.getApps());
                return "app/apps";
            }
            userAppList.add(app.get());
            userService.editUser(user);
        }

        return "redirect:/";
    }

    @GetMapping("/apps/{id}/unassign")
    public String unassignApp(@PathVariable Long id, Model model, @CookieValue(value = "id", defaultValue = "-1") String cookieId) {
        Optional<App> app = appService.getAppById(id);
        if (!app.isPresent()) {
            model.addAttribute("error", "There is no app with given id");
            return "app/apps";
        }

        Long parsedId = Long.parseLong(cookieId);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (loggedUser.isPresent()) {
            User user = loggedUser.get();
            if (user.getRole().equals(Role.ADMIN)) {
                List<List<Object>> list = createListOfUsers();
                model.addAttribute("error", "Admin cannot unassign because he doesnt contain them");
                model.addAttribute("isAdmin", user.getRole().equals(Role.ADMIN));
                model.addAttribute("logged", true);
                model.addAttribute("loggedUser", user);
                model.addAttribute("users", list);
                model.addAttribute("search", new Search());
                model.addAttribute("apps", appService.getApps());
                return "app/apps";
            }
            List<App> userAppList = user.getAppList();
            if (!userAppList.contains(app.get())) {
                List<List<Object>> list = createListOfUsers();
                model.addAttribute("error", "App is not in user list of apps");
                model.addAttribute("isAdmin", user.getRole().equals(Role.ADMIN));
                model.addAttribute("logged", true);
                model.addAttribute("loggedUser", user);
                model.addAttribute("users", list);
                model.addAttribute("search", new Search());
                model.addAttribute("apps", appService.getApps());
                return "app/apps";
            }
            userAppList.remove(app.get());
            userService.editUser(user);
        }

        return "redirect:/";
    }

    @GetMapping("/apps/create")
    public String appCreate(Model model, @CookieValue(value = "id", defaultValue = "-1") String id) {
        Long parsedId = Long.parseLong(id);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (!loggedUser.isPresent()) {
            return "redirect:/apps";
        }

        model.addAttribute("app", new App());
        model.addAttribute("action", "create");

        return "app/appForm";
    }

    @GetMapping("/apps/{id}/edit")
    public String appEdit(@PathVariable Long id, Model model, @CookieValue(value = "id", defaultValue = "-1") String cookieId) {
        Long parsedId = Long.parseLong(cookieId);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (!loggedUser.isPresent()) {
            return "redirect:/apps/" + id;
        }

        User user = loggedUser.get();

        if (!user.getRole().equals(Role.ADMIN)) {
            return "redirect:/apps/" + id;
        }

        Optional<App> app = appService.getAppById(id);
        if (!app.isPresent()) {
            model.addAttribute("error", "There is no app with given id");
            return "app/appForm";
        }

        model.addAttribute("app", app.get());
        model.addAttribute("action", "edit");

        return "app/appForm";
    }

    @PostMapping("/apps/create")
    public String createApp(@Valid App app, BindingResult errors, Model model, @CookieValue(value = "id", defaultValue = "-1") String cookieId) {
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

        Long parsedId = Long.parseLong(cookieId);

        Optional<User> loggedUser = userService.getUserById(parsedId);

        if (loggedUser.isPresent()) {
            appService.addApp(app);

            User user = loggedUser.get();
            if (user.getRole().equals(Role.USER)) {
                List<App> userAppList = user.getAppList();
                userAppList.add(app);
                user.setAppList(userAppList);
                userService.editUser(user);
            }
        }



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
