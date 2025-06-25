package com.t1.cardio.user.rest;

import com.t1.cardio.user.model.AppUser;
import com.t1.cardio.user.model.UserDTO;
import com.t1.cardio.user.controller.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.t1.cardio.user.model.FundDTO;
import com.t1.cardio.user.model.AuthDTO;

import java.util.List;

@RestController
public class UserRestCrt {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<AppUser> getUserById(@PathVariable Integer id) {
        System.out.println("/user/{id} GET params: "+ id);
        AppUser user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        System.out.println("/user/{id} DELETE params: "+ id);
        if (!userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<AppUser> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        System.out.println("/user/{id} PUT params: "+ id);
        AppUser appUser = userService.updateUser(userDTO);
        if (appUser != null) {
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<AppUser> createUser(@RequestBody UserDTO userDTO) {
        System.out.println("/user/ POST params: " + userDTO);
        AppUser newAppUser = userService.createUser(userDTO);
        if (newAppUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(newAppUser, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<AppUser>> getAllUsers() {
        System.out.println("/users GET params: ");
        List<AppUser> appUsers = userService.getAllUser();
        return new ResponseEntity<>(appUsers, HttpStatus.OK);
    }

    // Doit être privé
    @RequestMapping(value = "/user/funds", method = RequestMethod.PATCH)
    public ResponseEntity<String> patchUser(@RequestBody FundDTO fundDTO) {
        System.out.println("/user PATCH params: " + fundDTO);
        boolean result = userService.updateUserFund(fundDTO.getUserId(), fundDTO.getFunds());
        if (result) {
            return new ResponseEntity<>("Fund updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("An error occurred, check if the funds are positive", HttpStatus.NOT_FOUND);
        }
    }

    // Doit être privé
    @RequestMapping(value = "/user/auth", method = RequestMethod.POST)
    public ResponseEntity<Integer> getUserId(@RequestBody AuthDTO authDTO) {
        System.out.println("/user GET params: " + authDTO);
        Integer userId = userService.getUserId(authDTO.getUsername(), authDTO.getPassword());
        if (userId != null) {
            return new ResponseEntity<>(userId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
