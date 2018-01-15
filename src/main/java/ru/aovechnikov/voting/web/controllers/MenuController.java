package ru.aovechnikov.voting.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.service.DishService;
import ru.aovechnikov.voting.service.MenuService;
import ru.aovechnikov.voting.service.RestaurantService;
import ru.aovechnikov.voting.web.halresource.*;

import javax.validation.Valid;
import java.time.LocalDate;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static ru.aovechnikov.voting.util.DateTimeUtil.getCurrentDateIfNull;
import static ru.aovechnikov.voting.util.ValidationUtil.checkIdConsistent;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNew;

/**
 *  Rest controller provides endpoint for interaction with model {@link Menu}, {@link Dish}, {@link Restaurant}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */

@RestController
@RequestMapping(MenuController.MENU_URL)
public class MenuController {

    public static final String MENU_URL = "/rest/menus";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MenuResourceAssembler menuAssembler;

    @Autowired
    private RestaurantResourceAssembler restaurantAssembler;

    @Autowired
    private DishResourceAssembler dishAssembler;

    @Autowired
    private DishService dishService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    /**
     * Finds menu by {@code id}.
     *
     * @param id id of the menu to search for
     * @return {@link MenuResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<MenuResource> findById(@PathVariable("id") int id) {
        log.info("findById menu with id={}", id);
        MenuResource resource = menuAssembler.toResource(menuService.findById(id));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Deletes menu by {@code id}.
     *
     * @param id the id to delete for
     * @return {@link HttpStatus#OK}.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        log.info("delete menu with id={}", id);
        menuService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * Updates menu with {@code id}. If menu with {@code id} not exists in data store
     * return {@link HttpStatus#NOT_FOUND}. Field {@link Menu#restaurant} is not updated.
     * Returns {@link HttpStatus#INTERNAL_SERVER_ERROR} if identifier of {@code menu} and {@code id} differ.
     *
     * @param menu updated menu
     * @param id identifier of updated menu
     * @return {@link MenuResource} with status {@link HttpStatus#OK} if updated, {@link HttpStatus#CREATED} if created.
     */
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuResource> update(@PathVariable("id") int id, @RequestBody @Valid Menu menu) {
        log.info("update {} with id={}", menu, id);
        checkIdConsistent(menu, id);
        MenuResource resource = menuAssembler.toResource(menuService.update(menu));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Finds menus by {@code date}.
     *
     * @param date date to search for(default current date)
     * @param pageAssembler convert {@link Page} instances into {@link PagedResources}
     * @param pageable pagination information
     * @return {@link PagedResources} of {@link MenuResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/by-date", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<PagedResources<MenuResource>> findByDate(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                       LocalDate date, PagedResourcesAssembler<Menu> pageAssembler, Pageable pageable) {
        date = getCurrentDateIfNull(date);
        log.info("findByDate {} all menus with paging {}", date, pageable);
        Page<Menu> menusPage = menuService.findByDate(date, pageable);
        Link link = linkTo(methodOn(MenuController.class).findByDate(date, pageAssembler, pageable)).withSelfRel();
        return new ResponseEntity<>(pageAssembler.toResource(menusPage, menuAssembler, link), HttpStatus.OK);
    }

    /**
     * Finds menus by id of restaurant {@code restaurantId}.
     *
     * @param restaurantId identifier of restaurant to search for.
     * @param pageAssembler convert {@link Page} instances into {@link PagedResources}
     * @param pageable pagination information
     * @return {@link PagedResources} of {@link MenuResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/by-restaurant", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<PagedResources<MenuResource>> findByRestaurant(
            @RequestParam("id") int restaurantId, PagedResourcesAssembler<Menu> pageAssembler, Pageable pageable) {
        log.info("findByRestaurant all menus for restaurant with id={} with paging {}", restaurantId, pageable);
        Page<Menu> menusPage = menuService.findByRestaurantId(restaurantId, pageable);
        Link link = linkTo(methodOn(MenuController.class).findByRestaurant(restaurantId, pageAssembler, pageable)).withSelfRel();
        return new ResponseEntity<>(pageAssembler.toResource(menusPage, menuAssembler, link), HttpStatus.OK);
    }

    /**
     * Finds restaurant by id of menu {@code menuId}.
     *
     * @param menuId id of the menu to search for
     * @return {@link RestaurantResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/{id}/restaurant", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantResource> findRestaurantForMenu(@PathVariable("id") int menuId) {
        log.info("findRestaurantForMenu menu with id={}", menuId);
        RestaurantResource resource = restaurantAssembler.toResource(restaurantService.findByMenu(menuId));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Creates new {@code dish} for menu with {@code menuId}.
     * Returns {@link HttpStatus#INTERNAL_SERVER_ERROR} if {@code dish} has id.
     *
     * @param dish created dish
     * @param menuId identifier of menu
     * @return {@link DishResource} with {@link HttpStatus#CREATED}.
     */
    @PostMapping(value = "/{id}/dishes", produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishResource> createDishForMenu(@RequestBody @Valid Dish dish, @PathVariable("id") int menuId) {
        log.info("create {} for menu with id={}", dish, menuId);
        checkNew(dish);
        DishResource resource = dishAssembler.toResource(dishService.create(dish, menuId));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
}
