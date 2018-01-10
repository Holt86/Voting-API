package ru.aovechnikov.voting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.service.UserService;
import ru.aovechnikov.voting.to.UserTo;

@RestController
@RequestMapping(ProfileUserController.REST_URL)
public class ProfileUserController{
    public static final String REST_URL = "/rest/profile";

    @Autowired
    private UserService userService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> get() {
        return null;
    }

    @DeleteMapping
    public void delete() {

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody UserTo userTo) {

    }
}
