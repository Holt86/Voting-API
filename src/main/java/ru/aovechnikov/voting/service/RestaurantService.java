package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.util.exception.NotFoundException;
import ru.aovechnikov.voting.web.controllers.RestaurantController;


/**
 * Service interface for {@link Restaurant} domain objects.
 * Mostly used as a facade for {@link RestaurantController}. Also a placeholder for @Transactional
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */

public interface RestaurantService {

    /**
     * Retrieves an {@link Restaurant} by its id.
     *
     * @param id Value to search for.
     * @return {@link Restaurant} with the given id.
     * @throws NotFoundException if none found.
     */
    Restaurant findById(int id);
    /**
     * Delete an {@link Restaurant} to the data store by id.
     *
     * @param id the id to delete for
     * @throws NotFoundException if the number of deleted row is 0.
     */
    void delete(int id);

    /**
     * Saves {@link Restaurant}.
     *
     * @param restaurant must not be {@literal null}.
     * @return the saved {@link Restaurant} will never be {@literal null}.
     * @throws IllegalArgumentException in case the {@link Restaurant} is {@literal null}
     */
    Restaurant save(Restaurant restaurant);

    /**
     * Retrieve {@link Page} of {@link Restaurant} from the data store by part name.
     * If name is null throw {@link IllegalArgumentException}.
     *
     * @param name Value to search for
     * @param pageable pagination information
     * @return specified {@link Page(Restaurant)} whose name include part <code>name</code>
     */
    Page<Restaurant> findByName(String name, Pageable pageable);

    /**
     * Retrieve an {@link Restaurant} from the data store by menuId.
     * @param menuId the menuId to search for
     * @return the {@link Restaurant} if found or {@literal null}.
     */
    Restaurant findByMenu(int menuId);
}
