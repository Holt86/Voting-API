package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.aovechnikov.voting.web.controllers.MenuController;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.repository.RestaurantRepository;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.time.LocalDate;

/**
 * Service interface for {@link Menu} domain objects.
 * Mostly used as a facade for {@link MenuController}.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
public interface MenuService {

    /**
     * Retrieves an {@link Menu} by its id.
     *
     * @param id Value to search for.
     * @return {@link Menu} with the given id.
     * @throws NotFoundException if none found.
     */
    Menu findById(int id);

    /**
     * Delete an {@link Menu} to the data store by id.
     *
     * @param id the id to delete for
     * @throws NotFoundException if the number of deleted row is 0.
     */
    void delete(int id);

    /**
     * Assigns reference on {@link Menu#restaurant} with the given identifier
     * by invoke {@link RestaurantRepository#getOne(Object)}.
     * Saves {@link Menu}.
     *
     * @param menu must not be {@literal null}
     * @param restaurantId for get reference {@link Menu#restaurant}
     * @return the saved {@link Menu} will never be {@literal null}
     * @throws IllegalArgumentException in case the {@link Menu} is {@literal null}
     */
    Menu create(Menu menu, int restaurantId);

    /**
     * If an {@link Menu} exists in data store then it is updated,
     * otherwise throws {@link NotFoundException}.
     *
     * @param menu {@link Menu} which is updated
     * @return the updated {@link Menu} will never be {@literal null}
     * @throws NotFoundException If an {@link Menu} none exists.
     * @throws IllegalArgumentException in case the {@link Menu} is {@literal null}
     */
    Menu update(Menu menu);

    /**
     * Retrieve {@link Page} of {@link Menu} from the data store by id of {@link Restaurant}.
     *
     * @param restaurantId Value to search for
     * @param pageable pagination information
     * @return  {@link Page} all {@link Menu} which belongs to the {@link Restaurant}
     * with the specified id.
     */
    Page<Menu> findByRestaurantId(int restaurantId, Pageable pageable);

    /**
     * Retrieve {@link Page} of {@link Menu} from the data store by {@link LocalDate}.
     * If the {@link LocalDate} is {@literal null} throw {@link IllegalArgumentException}.
     *
     * @param date Value to search for
     * @param pageable pagination information
     * @return {@link Page} of all {@link Menu} which have the specified date.
     */
    Page<Menu> findByDate(LocalDate date, Pageable pageable);

    /**
     * Retrieve {@link Menu} from the data store by dishId of {@link Dish}
     *
     * @param dishId Value to search for
     * @return {@link Menu} which owns the {@link Dish} with the specified id.
     * @throws NotFoundException if none found.
     *
     */
    Menu findMenuByDish(int dishId);
}
