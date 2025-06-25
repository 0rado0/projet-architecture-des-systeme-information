package com.t1.cardio.user.controller;

import com.t1.cardio.user.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.t1.cardio.user.rest.CardRestClient;

import com.t1.cardio.user.model.UserDTO;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRestClient cardRestClient;

    public AppUser getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public AppUser createUser(UserDTO userDTO) {
        if (this.userExists(userDTO) || !userDTO.isValid()) {
            return null;
        }
        AppUser newAppUser = new AppUser(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), 500.00);
        AppUser newUserWithId = userRepository.save(newAppUser);
        cardRestClient.giveCardToUser(newUserWithId.getId());
        return newUserWithId;
    }

    private boolean userExists(UserDTO userDTO) {
        return userRepository.existsByUsername(userDTO.getUsername()) ||
                userRepository.existsByEmail(userDTO.getEmail());
    }

    public List<AppUser> getAllUser() {
        return (List<AppUser>) userRepository.findAll();
    }

    public boolean deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        } else {
            userRepository.deleteById(id);
            return true;
        }
    }

    public AppUser updateUser(UserDTO userDTO) {
        if (!userExists(userDTO) || !userDTO.isValid()) {
            return this.createUser(userDTO);
        }
        AppUser appUser = userRepository.findByUsername(userDTO.getUsername());
        if (appUser != null) {
            appUser.setUsername(userDTO.getUsername());
            appUser.setEmail(userDTO.getEmail());
            appUser.setPassword(userDTO.getPassword());
            return userRepository.save(appUser);
        }
        return null;
    }

    public Integer getUserId(String username, String password) {
        AppUser appUser = userRepository.findByUsernameAndPassword(username, password);
        if (appUser != null) {
            return appUser.getId();
        }
        return null;
    }

    public boolean updateUserFund(Integer userId, Double fund) {
        if (fund < 0) {
            return false;
        }

        AppUser appUser = userRepository.findById(userId).orElse(null);

        if (appUser != null) {
            appUser.setFunds(fund);
            userRepository.save(appUser);
            return true;
        } else {
            return false;
        }
    }
}
