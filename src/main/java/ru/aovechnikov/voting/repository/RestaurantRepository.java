package ru.aovechnikov.voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.Restaurant;

/**
 * Repository class for {@link Restaurant} domain objects.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    /**
     * Delete an {@link Restaurant} to the data store by id.
     * @param id the id to delete for
     * @return number deleted rows
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    /**
     * Retrieve {@link Page} of {@link Restaurant} from the data store by part name.
     * @param name Value to search for
     * @param pageable pagination information
     * @return specified {@link Page(Restaurant)} whose name include part <code>name</code>
     */
    @Query("SELECT r FROM Restaurant r WHERE UPPER(r.name) LIKE UPPER(CONCAT('%',:name,'%'))")
    Page<Restaurant> findByName(@Param("name")String name, Pageable pageable);


    /**
     * Retrieve an {@link Restaurant} from the data store by menuId.
     * @param menuId the menuId to search for
     * @return the {@link Restaurant} if found or {@literal null}.
     */
    @Query("SELECT r FROM Menu m JOIN m.restaurant r WHERE m.id=:menuId")
    Restaurant findByMenuId(@Param(("menuId"))int menuId);
}
