package com.wardyn.Projekt2.repositories;

import com.wardyn.Projekt2.domains.App;
import com.wardyn.Projekt2.domains.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends CrudRepository<App, Long> {

    App findAppById(Long id);
    List<App> findAllByUserListIsContaining(User user);
}
