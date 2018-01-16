package ru.aovechnikov.voting.web.servlet.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.AuthorizedUser;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.service.UserService;
import ru.aovechnikov.voting.to.UserTo;
import ru.aovechnikov.voting.web.servlet.halresource.UserResource;

import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static ru.aovechnikov.voting.util.ValidationUtil.checkIdConsistent;

/**
 *  Rest controller provides endpoint for interaction with model {@link User} with the authorization role {@code ROLE_USER}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@RestController
@RequestMapping(ProfileUserController.REST_URL)
public class ProfileUserController{
    public static final String REST_URL = "/rest/profile";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    /**
     * Finds authorized user.
     *
     * @param authorizedUser user to search for
     * @return {@link UserResource} with {@link HttpStatus#OK}.
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<UserResource> get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("get profile user with id={}", authorizedUser.getId());
        User user = userService.findById(authorizedUser.getId());
        UserResource resource = new UserResource(user);
        resource.add(linkTo(methodOn(this.getClass()).get(authorizedUser)).withSelfRel());
        resource.add(linkTo(methodOn(this.getClass()).get(authorizedUser)).withRel("profile"));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Deletes authorized user.
     *
     * @param authorizedUser deleted user
     * @return {@link HttpStatus#OK}.
     */
    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("delete user with id={}", authorizedUser.getId());
        userService.delete(authorizedUser.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates authorized user.
     * Returns {@link HttpStatus#INTERNAL_SERVER_ERROR} if identifier of
     * {@code user}and {@code authorizedUser} differ.
     *
     * @param userTo updated user
     * @param authorizedUser updatable user
     * @return {@link HttpStatus#OK}
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("update {} with id={}", userTo, authorizedUser.getId());
        checkIdConsistent(userTo, authorizedUser.getId());
        userService.update( userTo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
