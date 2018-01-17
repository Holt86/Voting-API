package ru.aovechnikov.voting.web.servlet.controllers;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Root controller for mapping on "/";
 *
 * @author - A.Ovechnikov
 * @date - 17.01.2018
 */
@RestController
public class RootController {

    /**
     * Redirect on "/rest"
     */
    @GetMapping(value = "/")
    public void root(HttpServletResponse response) throws IOException{
        response.sendRedirect("rest");
    }

    /**
     * Returns link's information about all controller's base endpoint for navigate.
     *
     */
    @GetMapping(value = "/rest", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<Resource<Object>> rootRest(){
        Resource<Object> resource = new Resource<>(new Object());
        Link linkUsers = linkTo(AdminUserController.class).withRel("users");
        Link linkProfile = linkTo(ProfileUserController.class).withRel("profile");
        Link linkRestaurants = linkTo(RestaurantController.class).withRel("restaurants");
        Link linkMenus = linkTo(MenuController.class).withRel("menus");
        Link linkDishes = linkTo(DishController.class).withRel("dishes");
        Link linkVotes = linkTo(methodOn(VoteController.class).getVoteForUserByDate(null, null)).withRel("vote");
        Link linkResult = linkTo(methodOn(VoteController.class).getResultToByDate(null)).withRel("result");
        resource.add(linkUsers, linkProfile, linkRestaurants, linkMenus, linkDishes, linkVotes, linkResult);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
