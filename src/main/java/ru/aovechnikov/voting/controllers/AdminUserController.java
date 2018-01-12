package ru.aovechnikov.voting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.service.UserService;

import java.util.List;

@RestController
@RequestMapping(AdminUserController.REST_URL)
public class AdminUserController {
    public static final String REST_URL = "/rest/admin/users";

    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User findById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        userService.delete(id);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> update(@PathVariable("id") int id, @RequestBody User user) {
        userService.save(user);
        return null;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User user) {
        userService.save(user);
        return null;
    }

    @GetMapping(value = "/by-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public User findByEmail(@RequestParam("email") String email) {
        return userService.findByEmail(email);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> findAll(Pageable pageable){

        return userService.findAll(pageable).getContent();
    }

    @PatchMapping(value = "/{id}/enabled")
    public void enable(@PathVariable("id") int id, @RequestParam(value = "enabled") boolean enabled) {
       userService.enable(id, enabled);
    }
}
