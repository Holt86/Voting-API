package ru.aovechnikov.voting.web.servlet.controllers;

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
import ru.aovechnikov.voting.service.DishService;
import ru.aovechnikov.voting.service.MenuService;
import ru.aovechnikov.voting.web.servlet.halresource.DishResource;
import ru.aovechnikov.voting.web.servlet.halresource.DishResourceAssembler;
import ru.aovechnikov.voting.web.servlet.halresource.MenuResource;
import ru.aovechnikov.voting.web.servlet.halresource.MenuResourceAssembler;

import javax.validation.Valid;
import java.time.LocalDate;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static ru.aovechnikov.voting.util.DateTimeUtil.getCurrentDateIfNull;
import static ru.aovechnikov.voting.util.ValidationUtil.checkIdConsistent;

/**
 *  Rest controller provides endpoint for interaction with model {@link Dish}, {@link Menu}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
@RestController
@RequestMapping(DishController.URL_DISH)
public class DishController {

    public static final String URL_DISH = "/rest/dishes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishService dishService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private DishResourceAssembler dishAssembler;

    @Autowired
    private MenuResourceAssembler menuAssembler;

    /**
     * Finds dish by {@code id}.
     *
     * @param id id of the dish to search for

     * @return {@link DishResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<DishResource> findById(@PathVariable("id") int id) {
        log.info("findById dish with id={}", id);
        DishResource resource = dishAssembler.toResource(dishService.findById(id));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Deletes dish by {@code id}.
     *
     * @param id the id to delete for
     * @return {@link HttpStatus#OK}.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id){
        log.info("delete dish with id={}", id);
        dishService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * Updates dish with {@code id}. If dish with {@code id} not exists in data store
     * return {@link HttpStatus#NOT_FOUND}. Field {@link Dish#menu} is not updated.
     * {@link HttpStatus#INTERNAL_SERVER_ERROR} if identifier of {@code dish} and {@code id} differ.
     *
     * @param dish updated dish
     * @param id identifier of updated dish
     * @return {@link DishResource} with status {@link HttpStatus#OK} if updated, {@link HttpStatus#CREATED} if created.
     */
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishResource> update(@RequestBody @Valid Dish dish, @PathVariable("id") int id){
        log.info("update {} with id={}", dish, id);
        checkIdConsistent(dish, id);
        DishResource resource = dishAssembler.toResource(dishService.update(dish));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    /**
     * Finds dishes by {@code date}.
     *
     * @param date date to search for(default current date)
     * @param pageAssembler convert {@link Page} instances into {@link PagedResources}
     * @param pageable pagination information
     * @return {@link PagedResources} of {@link DishResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/by-date", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<PagedResources<DishResource>> findByDate(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                   LocalDate date, PagedResourcesAssembler<Dish> pageAssembler, Pageable pageable){
        date = getCurrentDateIfNull(date);
        log.info("findByDate {} all dishes with paging {}", date, pageable);
        Page<Dish> dishesPage = dishService.findByDate(date, pageable);
        Link link = linkTo(methodOn(DishController.class).findByDate(date, pageAssembler, pageable)).withSelfRel();
        return new ResponseEntity<>(pageAssembler.toResource(dishesPage, dishAssembler, link), HttpStatus.OK);
    }

    /**
     * Finds dishes by id of menu {@code menuId}.
     *
     * @param menuId identifier of menu to search for
     * @param pageAssembler convert {@link Page} instances into {@link PagedResources}
     * @param pageable pagination information
     * @return {@link PagedResources} of {@link DishResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/by-menu", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<PagedResources<DishResource>> findByMenu(@RequestParam("id") int menuId, PagedResourcesAssembler<Dish> pageAssembler, Pageable pageable) {
        log.info("findByMenu all dishes for menu with id={} with paging {}", menuId, pageable);
        Page<Dish> pageDish = dishService.findByMenu(menuId, pageable);
        Link link = linkTo(methodOn(DishController.class).findByMenu(menuId, pageAssembler, pageable)).withSelfRel();
        return new ResponseEntity<>(pageAssembler.toResource(pageDish, dishAssembler, link), HttpStatus.OK);
    }

    /**
     * Finds menu by id of dish {@code dishId}.
     *
     * @param dishId dishId of the dish to search for
     * @return {@link MenuResource} with status {@link HttpStatus#OK}.
     */
    @GetMapping(value = "/{id}/menu", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    public ResponseEntity<MenuResource> findMenuForDish(@PathVariable("id") int dishId){
        log.info("findMenuForDish dish with id={}", dishId);
        MenuResource resource = menuAssembler.toResource(menuService.findMenuByDish(dishId));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}

