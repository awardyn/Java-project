package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.repositories.UserRepository;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final AppService appService;
    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, @Lazy AppService appService) {
        this.appService = appService;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        this.userRepository.findAll().forEach(userList::add);
        return userList;
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findUserById(id);
    }

    @Override
    public void addUser(User user) {
        this.userRepository.save(user);
    }

    @Override
    public Boolean editUser(User user) {
        User userToEdit = getUserById(user.getId());

        if (userToEdit == null) {
            return false;
        }

        user.setId(userToEdit.getId());

        this.userRepository.save(user);

        return true;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        User user = getUserById(userId);

        if (user == null) {
            return false;
        }

        this.userRepository.delete(user);

        return true;
    }

    @Override
    public List<User> getUsersByAppId(App app) {
        return this.userRepository.findAllByAppListIsContaining(app);
    }
}
