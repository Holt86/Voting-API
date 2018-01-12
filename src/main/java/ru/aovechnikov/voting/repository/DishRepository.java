package ru.aovechnikov.voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.model.Menu;

import java.time.LocalDate;

/**
 * Repository class for {@link Dish} domain objects
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer>{

    /**
     * Delete an {@link Dish} to the data store by id.
     *
     * @param id the id to delete for
     * @return number deleted rows
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);

    /**
     * Retrieve {@link Page} of {@link Dish} from the data store by id of {@link Menu}.
     *
     * @param menuId Value to search for
     * @param pageable pagination information
     * @return  {@link Page} all {@link Dish} which belongs to the {@link Menu}
     * with the specified id.
     */
    @Query("SELECT d FROM Dish d JOIN d.menu m WHERE d.menu.id=:menuId")
    Page<Dish> findByMenuId(@Param("menuId") int menuId, Pageable pageable);

    /**
     * Retrieve {@link Page} of {@link Dish} from the data store by {@link LocalDate}.
     *
     * @param date Value to search for
     * @param pageable pagination information
     * @return {@link Page} of all {@link Menu} which have the specified date.
     */
    @Query("SELECT d FROM Dish d JOIN d.menu m WHERE d.menu.date=:date")
    Page<Dish> findByDate(@Param("date") LocalDate date, Pageable pageable);
}
