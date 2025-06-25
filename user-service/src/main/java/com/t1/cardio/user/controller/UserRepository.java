package com.t1.cardio.user.controller;

import com.t1.cardio.user.model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Integer> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    AppUser findByUsername(String username);

    AppUser findByUsernameAndPassword(String username, String password);
}
