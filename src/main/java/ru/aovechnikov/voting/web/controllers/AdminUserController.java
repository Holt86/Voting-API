package ru.aovechnikov.voting.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.service.UserService;
import ru.aovechnikov.voting.web.halresource.UserResource;
import ru.aovechnikov.voting.web.halresource.UserResourceAssembler;

import javax.validation.Valid;

import static ru.aovechnikov.voting.util.ValidationUtil.checkIdConsistent;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNew;

/**
 * Rest controller provides endpoint for interaction with model {@link User} with the authorization role {@code ROLE_ADMIN}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@RestController
@RequestMapping(AdminUserController.REST_URL)
public class AdminUserController{
    public static final String REST_URL = "/rest/admin/users";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserResourceAssembler userAssembler;
    @Autowired
    private UserService userService;

    /**
     * Finds user by {@code id}.
     *
     * @param id id of the user to search for
     * @return {@link UserResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<UserResource> findById(@PathVariable("id") int id) {
        log.info("findById user with id={}", id);
        UserResource resource = userAssembler.toResource(userService.findById(id));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Deletes user by {@code id}.
     *
     * @param id the id to deletable user
     * @return {@link HttpStatus#OK}.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        log.info("delete user with id={}", id);
        userService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     *
     * If the user with {@code id} exists in the data store then updates it,
     * otherwise created new.
     * Returns {@link HttpStatus#INTERNAL_SERVER_ERROR} if identifier of {@code user} and {@code id} differ.
     *
     * @param user updated user
     * @return {@link UserResource} with status {@link HttpStatus#OK} if updated, {@link HttpStatus#CREATED} if created.
     */
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResource> update(@PathVariable("id") int id, @Valid @RequestBody User user) {
        log.info("update {} with id={}", user, id);
        checkIdConsistent(user, id);
        user = userService.save(user);
        UserResource resource = userAssembler.toResource(user);
        return new ResponseEntity<>(resource, user.getId() == id ? HttpStatus.OK : HttpStatus.CREATED);
    }

    /**
     * Creates new {@code user}.
     * Returns {@link HttpStatus#INTERNAL_SERVER_ERROR} if {@code user} has id.
     *
     * @param user created user
     * @return created {@link UserResource} with status {@link HttpStatus#CREATED}.
     */
    @PostMapping(produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResource> create(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        UserResource resource = userAssembler.toResource(userService.save(user));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    /**
     * Finds user by {@code email}.
     *
     * @param email email of the user to search for
     * @return {@link UserResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/by-email", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<UserResource> findByEmail(@RequestParam("email") String email) {
        log.info("findById user by email={}", email);
        UserResource resource = userAssembler.toResource(userService.findByEmail(email));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Finds all user meeting the paging restriction.
     *
     * @param pageAssembler convert {@link Page} instances into {@link PagedResources}
     * @param pageable pagination information
     * @return {@link PagedResources} of {@link UserResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<PagedResources<UserResource>> findAll(PagedResourcesAssembler<User> pageAssembler, Pageable pageable) {
        log.info("findAll users");
        Page<User> usersPage = userService.findAll(pageable);
        return new ResponseEntity<>(pageAssembler.toResource(usersPage, userAssembler), HttpStatus.OK);
    }

    /**
     * Updates {@link User#enabled} property of user with specified id.
     *
     * @param id id of the user to search for
     * @param enabled updated property
     * @return {@link HttpStatus#OK}.
     */
    @PatchMapping(value = "/{id}/enabled")
    public ResponseEntity<Void> enable(@PathVariable("id") int id, @RequestParam(value = "enabled", defaultValue = "false") boolean enabled){
        log.info((enabled ? "enable " : "disable ") + id);
        userService.enable(id, enabled);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
