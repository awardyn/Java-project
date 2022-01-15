package com.wardyn.Projekt2.repositories;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserById(Long id);
    List<User> findAllByAppListIsContaining(App app);
    User findUserByUsernameAndUserPassword(String username, String userPassword);
}
