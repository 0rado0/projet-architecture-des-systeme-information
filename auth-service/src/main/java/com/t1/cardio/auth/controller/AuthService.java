package com.t1.cardio.auth.controller;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.t1.cardio.auth.rest.UserRestClient;

import com.t1.cardio.auth.model.AuthDTO;

@Service
public class AuthService {

    @Autowired
    private UserRestClient userRestClient;

    public Integer authenticateUser(AuthDTO authDTO) {
        Integer userId = userRestClient.getUserId(authDTO.getUsername(), authDTO.getPassword()).block();
        if (userId != null) {
            System.out.println("User authenticated successfully with ID: " + userId);
            return userId;
        } else {
            System.out.println("Authentication failed for user: " + authDTO.getUsername());
            return null;
        }
    }
}
