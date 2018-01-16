package ru.aovechnikov.voting.web.servlet.controllers;


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
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.service.MenuService;
import ru.aovechnikov.voting.service.RestaurantService;
import ru.aovechnikov.voting.web.servlet.halresource.MenuResource;
import ru.aovechnikov.voting.web.servlet.halresource.MenuResourceAssembler;
import ru.aovechnikov.voting.web.servlet.halresource.RestaurantResource;
import ru.aovechnikov.voting.web.servlet.halresource.RestaurantResourceAssembler;

import javax.validation.Valid;

import static ru.aovechnikov.voting.util.ValidationUtil.checkIdConsistent;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNew;

/**
 *  Rest controller provides endpoint for interaction with model {@link Restaurant} and {@link Menu}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@RestController
@RequestMapping( RestaurantController.URL_REST)
public class RestaurantController{

    public static final String URL_REST = "/rest/restaurants";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantResourceAssembler restaurantResourceAssembler;

    @Autowired
    private MenuResourceAssembler menuResourceAssembler;

    /**
     * Finds all restaurants whose name is like {@code name}.
     *
     * @param pageAssembler convert {@link Page} instances into {@link PagedResources}
     * @param pageable pagination information
     * @param name part of the name to search for(default is empty).
     * @return {@link PagedResources} of {@link RestaurantResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/by-name", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<PagedResources<RestaurantResource>> findByName(PagedResourcesAssembler<Restaurant> pageAssembler, Pageable pageable,
                                                                         @RequestParam(value = "name", defaultValue = "") String name) {
        log.info("findByName restaurant with name={}", name);
        Page<Restaurant> pageRestaurants = restaurantService.findByName(name, pageable);
        return new ResponseEntity<>(pageAssembler.toResource(pageRestaurants, restaurantResourceAssembler), HttpStatus.OK);
    }

    /**
     * Finds restaurant by {@code id}.
     *
     * @param id id of the restaurant to search for
     * @return {@link RestaurantResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantResource> findById(@PathVariable("id") int id) {
        log.info("findById restaurant with id={}", id);
        RestaurantResource resource = restaurantResourceAssembler.toResource(restaurantService.findById(id));
        return new ResponseEntity<RestaurantResource>(resource, HttpStatus.OK);
    }

    /**
     * Deletes restaurant by {@code id}.
     *
     * @param id the id to delete for
     * @return {@link HttpStatus#OK}.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        log.info("delete restaurant with id={}", id);
        restaurantService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Creates new {@code restaurant}.
     * Returns {@link HttpStatus#INTERNAL_SERVER_ERROR} if {@code restaurant} has id.
     *
     * @param restaurant created restaurant
     * @return created {@link RestaurantResource} with status {@link HttpStatus#CREATED}.
     */
    @PostMapping(produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantResource> create(@RequestBody @Valid Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        RestaurantResource resource = restaurantResourceAssembler.toResource(restaurantService.save(restaurant));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    /**
     *
     * If the restaurant with {@code id} exists in the data store then updates it,
     * otherwise created new.
     * Returns {@link HttpStatus#INTERNAL_SERVER_ERROR} if identifier of {@code restaurant} and {@code id} differ.
     *
     * @param restaurant updated restaurant
     * @return {@link RestaurantResource} with status {@link HttpStatus#OK} if updated, {@link HttpStatus#CREATED} if created.
     */
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantResource> update(@RequestBody @Valid Restaurant restaurant, @PathVariable("id") int id) {
        log.info("update {} with id={}", restaurant, id);
        checkIdConsistent(restaurant, id);
        restaurant = restaurantService.save(restaurant);
        RestaurantResource resource = restaurantResourceAssembler.toResource(restaurant);
        return new ResponseEntity<>(resource, restaurant.getId() == id ? HttpStatus.OK : HttpStatus.CREATED);
    }

    /**
     * Created new {@code menu} for restaurant with {@code restaurantId}.
     * Returns {@link HttpStatus#INTERNAL_SERVER_ERROR} if {@code menu} has id.
     *
     * @param menu created menu
     * @param restaurantId identifier of restaurant
     * @return {@link MenuResource} with {@link HttpStatus#CREATED}.
     */
    @PostMapping(value = "/{id}/menus", produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuResource> createMenuForRestaurants(@RequestBody @Valid Menu menu, @PathVariable("id") int restaurantId) {
        log.info("create {} for restaurant with id={}", menu, restaurantId);
        checkNew(menu);
        MenuResource resource = menuResourceAssembler.toResource(menuService.create(menu, restaurantId));
        return new ResponseEntity<MenuResource>(resource, HttpStatus.CREATED);
    }
}
