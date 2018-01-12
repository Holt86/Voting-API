package ru.aovechnikov.voting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.User;

import java.util.Optional;

/**
 * Repository class for {@link User} domain objects
 *
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Delete an {@link User} to the data store by id.
     *
     * @param id the id to delete for
     * @return number deleted rows
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);
    /**
     * Retrieves an {@link User} by its email.
     *
     * @param email must not be {@literal null}.
     * @return {@link User} with the given email or {@literal Optional#empty()} if none found
     * @throws IllegalArgumentException if {@code email} is {@literal null}.
     */
    @Query("SELECT u FROM User u WHERE u.email=:email")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Returns a {@link Page} of {@link User} meeting the paging restriction
     * provided in the {@code Pageable} object.
     *
     * @param pageable pagination information
     * @return a page of {@link User}
     */
    @Override
    Page<User> findAll(Pageable pageable);
}
