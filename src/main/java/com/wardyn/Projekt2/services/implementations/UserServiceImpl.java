package com.wardyn.Projekt2.services.implementations;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import com.wardyn.Projekt2.enums.Role;
import com.wardyn.Projekt2.repositories.UserRepository;
import com.wardyn.Projekt2.services.interfaces.AppService;
import com.wardyn.Projekt2.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        this.userRepository.findAll().forEach(userList::add);
        return userList;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public void addUser(User user) {
        user.setRole(Role.USER);
        this.userRepository.save(user);
    }

    @Override
    public Boolean editUser(User user) {
        Optional<User> optionalUser = getUserById(user.getId());

        if (!optionalUser.isPresent()) {
            return false;
        }
        optionalUser.ifPresent(userToEdit -> {
            user.setId(userToEdit.getId());

            this.userRepository.save(user);
        });

        return true;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        Optional<User> user = getUserById(userId);

        if (!user.isPresent()) {
            return false;
        }

        user.ifPresent(this.userRepository::delete);

        return true;
    }

    @Override
    public List<User> getUsersByAppId(App app) {
        return this.userRepository.findAllByAppListIsContaining(app);
    }
}
