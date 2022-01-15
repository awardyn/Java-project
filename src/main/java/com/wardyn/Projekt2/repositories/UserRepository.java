package com.wardyn.Projekt2.repositories;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAllByAppListIsContaining(App app);
    Optional<User> findUserByUsernameAndUserPassword(String username, String userPassword);
}
