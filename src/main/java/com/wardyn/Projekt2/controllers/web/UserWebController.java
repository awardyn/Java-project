package com.wardyn.Projekt2.controllers.web;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.Search;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class UserWebController {
    private final UserService userService;
    private final AppService appService;

    @Autowired
    public UserWebController(UserService userService, AppService appService) {
        this.userService = userService;
        this.appService = appService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {

        List<List<Object>> list = createListOfApps();

        model.addAttribute("apps", list);
        model.addAttribute("search", new Search());
        model.addAttribute("users", userService.getUsers());

        return "user/users";
    }

    @GetMapping("/users/search")
    public String getUsersByUsername(Search search, Model model) {
        if(search.getSearchBy().equals(-1)) {
            return "redirect:/users";
        }

        List<List<Object>> list = createListOfApps();

        model.addAttribute("apps", list);
        model.addAttribute("search", new Search(search.getSearchBy()));
        model.addAttribute("users", userService.getUsersByAppId(search.getSearchBy()));

        return "user/users";
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            model.addAttribute("error", "There is no user with given id");
            return "user/user";
        }

        model.addAttribute("user", user);
        model.addAttribute("apps", appService.getUserApps(user.getAppList()));

        return "user/user";
    }

    @GetMapping("/users/create")
    public String userCreate(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("apps", appService.getApps());
        model.addAttribute("action", "create");

        return "user/userForm";
    }

    @GetMapping("/users/{id}/edit")
    public String userEdit(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            model.addAttribute("error", "There is no user with given id");
            return "user/userForm";
        }

        model.addAttribute("user", user);
        model.addAttribute("apps", appService.getApps());
        model.addAttribute("action", "edit");

        return "user/userForm";
    }

    @PostMapping("/users/create")
    public String createUser(@Valid User user, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("apps", appService.getApps());
            model.addAttribute("action", "create");
            return "user/userForm";
        }

        List<String> usernames = userService.getUsers().stream().map(User::getUsername).collect(Collectors.toList());

        if (usernames.contains(user.getUsername())) {
            ObjectError error = new ObjectError("domain", "Username is not unique");
            errors.addError(error);
            model.addAttribute("apps", appService.getApps());
            model.addAttribute("action", "create");
            return "user/userForm";
        }


        userService.addUser(user);

        return "redirect:/users";
    }

    @PostMapping("/users/{id}/edit")
    public String editUser(@Valid User user, BindingResult errors, RedirectAttributes redirectAttributes, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("apps", appService.getApps());
            model.addAttribute("action", "edit");
            return "user/userForm";
        }

        List<String> usernames = userService.getUsers().stream().filter(u -> !Objects.equals(u.getId(), user.getId())).map(User::getUsername).collect(Collectors.toList());

        if (usernames.contains(user.getUsername())) {
            ObjectError error = new ObjectError("domain", "Username is not unique");
            errors.addError(error);
            model.addAttribute("apps", appService.getApps());
            model.addAttribute("action", "create");
            return "user/userForm";
        }

        Boolean edited = userService.editUser(user);

        if (edited.equals(false)) {
            redirectAttributes.addFlashAttribute("error", "There is no user with given id");
            return "redirect:/users/" + user.getId() + "/edit";
        }

        return "redirect:/users/" + user.getId();
    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Boolean deleted = userService.deleteUser(id);
        if (deleted.equals(false)) {
            redirectAttributes.addFlashAttribute("error", "There is no user with given id to delete");
            return "redirect:/users";
        }
        return "redirect:/users";
    }

    private List<List<Object>> createListOfApps() {
        List<List<Object>> list = new ArrayList<>();
        List<Object> all = new ArrayList<>();
        all.add(-1);
        all.add("all");

        list.add(all);

        List<App> appList = appService.getApps();

        for (App app : appList) {
            List<Object> element = new ArrayList<>();
            element.add(app.getId());
            element.add(app.getName());
            list.add(element);
        }

        return list;
    }
}
