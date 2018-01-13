package ru.aovechnikov.voting.web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.service.MenuService;
import ru.aovechnikov.voting.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping( RestaurantController.URL_REST)
public class RestaurantController{

    public static final String URL_REST = "/rest/restaurants";

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;


    @GetMapping(value = "/by-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> findByName(Pageable pageable, @RequestParam(value = "name") String name) {
        return restaurantService.findByName(name, pageable).getContent();
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant findById(@PathVariable("id") int id) {
        return restaurantService.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant create(@RequestBody Restaurant restaurant) {
        restaurantService.save(restaurant);
        return null;
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> update(@RequestBody Restaurant restaurant, @PathVariable("id") int id) {
        restaurantService.save(restaurant);
        return null;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        restaurantService.delete(id);
    }

    @PostMapping(value = "/{id}/menus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createMenuForRestaurants(@RequestBody Menu menu, @PathVariable("id") int restaurantId) {
        menuService.create(menu, restaurantId);
        return null;
    }

}
