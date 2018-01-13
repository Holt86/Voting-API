package ru.aovechnikov.voting.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.service.DishService;
import ru.aovechnikov.voting.service.MenuService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(DishController.URL_DISH)
public class DishController {

    public static final String URL_DISH = "/rest/dishes";

    @Autowired
    private DishService dishService;

    @Autowired
    private MenuService menuService;


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Dish findById(@PathVariable("id") int id) {

        return dishService.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id){
    dishService.delete(id);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> update(@RequestBody Dish dish, @PathVariable("id") int id){
        dishService.update(dish);
        return null;
    }

    @GetMapping(value = "/by-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> findByDate(@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                       LocalDate date, Pageable pageable){
        return dishService.findByDate(date, pageable).getContent();
    }

    @GetMapping(value = "/by-menu", produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> findDishesForMenu(@RequestParam("id") int id, Pageable pageable) {

        return dishService.findByMenu(id, pageable).getContent();
    }

    @GetMapping(value = "/{id}/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public Menu findMenuForDish(@PathVariable("id") int id){
        return menuService.findMenuByDish(id);
    }

}
