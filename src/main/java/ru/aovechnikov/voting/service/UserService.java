package ru.aovechnikov.voting.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.aovechnikov.voting.web.servlet.controllers.AdminUserController;
import ru.aovechnikov.voting.web.servlet.controllers.ProfileUserController;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.to.UserTo;
import ru.aovechnikov.voting.util.UserUtil;
import ru.aovechnikov.voting.util.exception.NotFoundException;

/**
 * Service interface for {@link User} domain objects.
 * Mostly used as a facade for {@link AdminUserController} and {@link ProfileUserController}.
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public interface UserService {
    /**
     * Retrieves an {@link User} by its id.
     *
     * @param id Value to search for.
     * @return {@link User} with the given id.
     * @throws NotFoundException if none found.
     */
    User findById(int id);

    /**
     * Delete an {@link User} to the data store by id.
     *
     * @param id the id to delete for
     * @throws NotFoundException if the number of deleted row is 0.
     */
    void delete(int id);

    /**
     * {@link UserTo} is converted into {@link User} invoked {@link UserUtil#updateFromTo(User, UserTo)}
     * and saves in the data store.
     *
     * @param userTo {@link UserTo} which is updated
     * @throws NotFoundException if an {@link User} with {@link UserTo#getId()} none exists in the data store.
     * @throws IllegalArgumentException in case the {@link UserTo} is {@literal null}
     */
    void update(UserTo userTo);

    /**
     * Saves {@link User}.
     *
     * @param user must not be {@literal null}.
     * @return the saved {@link User} will never be {@literal null}.
     * @throws IllegalArgumentException in case the {@link User} is {@literal null}
     */
    User save(User user);

    /**
     * Retrieves an {@link User} by its email.
     *
     * @param email Value to search for.
     * @return {@link User} with the given id.
     * @throws NotFoundException if none found.
     * @throws IllegalArgumentException in case the email is {@literal null}
     */
    User findByEmail(String email);


    /**
     * Updates {@link User#enabled} property of {@link User} with specified id.
     *
     * @param id identify of user
     * @param enable updated property
     * @throws NotFoundException if {@link User} none found.
     */
    void enable(int id, boolean enable);
    /**
     * Retrieves {@link Page} of {@link User} meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable pagination information
     * @return page of {@link User}.
     */
    Page<User> findAll(Pageable pageable);
}
