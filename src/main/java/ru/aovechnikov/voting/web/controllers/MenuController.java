package ru.aovechnikov.voting.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.service.DishService;
import ru.aovechnikov.voting.service.MenuService;
import ru.aovechnikov.voting.service.RestaurantService;
import ru.aovechnikov.voting.to.DishTo;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(MenuController.MENU_URL)
public class MenuController {

    public static final String MENU_URL = "/rest/menus";

    @Autowired
    private DishService dishService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Menu findById(@PathVariable("id") int id) {
        return menuService.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
      menuService.delete(id);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> update(@PathVariable("id") int id, @RequestBody Menu menu) {
        menuService.update(menu);
        return null;
    }

    @GetMapping(value = "/by-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> findMenusByDate(@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                            LocalDate date, Pageable pageable) {
        return menuService.findByDate(date, pageable).getContent();
    }

    @GetMapping(value = "/by-restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> findMenusByRestaurant(
            @RequestParam("id") int id, Pageable pageable) {
        return menuService.findByRestaurantId(id, pageable).getContent();
    }

    @GetMapping(value = "/{id}/restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant findRestaurantByMenu(@PathVariable("id") int menuId) {
        return restaurantService.findByMenu(menuId);
    }

    @PostMapping(value = "/{id}/dishes", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createDishForMenu(@RequestBody Dish dish, @PathVariable("id") int menuId) {
        dishService.create(dish, menuId);
        return null;
    }

}
