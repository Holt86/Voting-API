package ru.aovechnikov.voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.Restaurant;

import javax.persistence.QueryHint;
import java.time.LocalDate;

/**
 * Repository class for {@link Menu} domain objects.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer>{

    /**
     * Delete an {@link Menu} to the data store by id.
     *
     * @param id the id to delete for
     * @return number deleted rows
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    /**
     * Retrieve {@link Page} of {@link Menu} from the data store by id of {@link Restaurant}.
     * Hibernate caches query level.
     *
     * @param restaurantId Value to search for
     * @param pageable pagination information
     * @return  {@link Page} all {@link Menu} which belongs to the {@link Restaurant}
     * with the specified id.
     */
    @Query("SELECT m FROM Menu m JOIN m.restaurant WHERE m.restaurant.id=:restaurantId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<Menu> findByRestaurantId(@Param("restaurantId") int restaurantId, Pageable pageable);

    /**
     * Retrieve {@link Page} of {@link Menu} from the data store by {@link LocalDate}.
     * Hibernate caches query level.
     *
     * @param date Value to search for
     * @param pageable pagination information
     * @return {@link Page} of all {@link Menu} which have the specified date.
     */
    @Query("SELECT m FROM Menu m JOIN m.restaurant WHERE m.date=:date")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<Menu> findByDate(@Param("date") LocalDate date, Pageable pageable);

    /**
     * Retrieve {@link Menu} from the data store by dishId of {@link Dish}
     *
     * @param dishId Value to search for
     * @return {@link Menu} which owns the {@link Dish} with the specified id
     * or {@literal null} if none found.
     */
    @Query("SELECT m FROM Dish d JOIN d.menu m WHERE d.id=:id")
    Menu findMenuByDish(@Param("id") int dishId);
}
