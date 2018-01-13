package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.aovechnikov.voting.controllers.DishController;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.repository.DishRepository;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.time.LocalDate;
/**
 * Service interface for {@link Dish} domain objects.
 * Mostly used as a facade for {@link DishController}. Also a placeholder for @Transactional.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */

public interface DishService {

    /**
     * Retrieves an {@link Dish} by its id.
     *
     * @param id Value to search for.
     * @return {@link Dish} with the given id.
     * @throws NotFoundException if none found.
     */
    Dish findById(int id);

    /**
     * Delete an {@link Dish} to the data store by id.
     *
     * @param id the id to delete for
     * @throws NotFoundException if the number of deleted row is 0.
     */
    void delete(int id);

    /**
     * Assigns reference on {@link Dish#menu} with the given identifier
     * by invoke {@link DishRepository#getOne(Object)}.
     * Saves {@link Dish}.
     *
     * @param dish must not be {@literal null}
     * @param menuId for get reference {@link Dish#menu}
     * @return the saved {@link Dish} will never be {@literal null}
     * @throws IllegalArgumentException in case the {@link Dish} is {@literal null}
     */
    Dish create(Dish dish, int menuId);

    /**
     * If an {@link Dish} exists in data store then it is updated,
     * otherwise throws {@link NotFoundException}.
     *
     * @param dish {@link Dish} which is updated
     * @return the updated {@link Dish} will never be {@literal null}
     * @throws NotFoundException If an {@link Dish} none exists.
     * @throws IllegalArgumentException in case the {@link Dish} is {@literal null}
     */
    Dish update(Dish dish);

    /**
     * Retrieve {@link Page} of {@link Dish} from the data store by id of {@link Menu}.
     *
     * @param menuId Value to search for
     * @param pageable pagination information
     * @return  {@link Page} all {@link Dish} which belongs to the {@link Menu}
     * with the specified id.
     */
    Page<Dish> findByMenu(int menuId, Pageable pageable);

    /**
     * Retrieve {@link Page} of {@link Dish} from the data store by {@link LocalDate}.
     * If the {@link LocalDate} is {@literal null} throw {@link IllegalArgumentException}.
     *
     * @param date Value to search for
     * @param pageable pagination information
     * @return {@link Page} of all {@link Dish} which have the specified date.
     */
    Page<Dish> findByDate(LocalDate date, Pageable pageable);
}
