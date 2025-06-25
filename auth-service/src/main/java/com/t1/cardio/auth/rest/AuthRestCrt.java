package com.t1.cardio.auth.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.t1.cardio.auth.model.AuthDTO;
import com.t1.cardio.auth.controller.AuthService;

@RestController
public class AuthRestCrt {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<Integer> authenticate(@RequestBody AuthDTO authDTO) {
        System.out.println("/auth POST params: " + authDTO);
        Integer result = authService.authenticateUser(authDTO);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
