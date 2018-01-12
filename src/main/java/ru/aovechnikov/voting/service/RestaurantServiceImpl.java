package ru.aovechnikov.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.repository.RestaurantRepository;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import static ru.aovechnikov.voting.util.ValidationUtil.checkNotFound;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNotFoundWithId;

/**
 * Implementation of the {@link RestaurantService}
 *
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private RestaurantRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Restaurant findById(int id) {
        return repository.findById(id).orElseThrow(() ->
                new NotFoundException("id=" + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(int id) {
        checkNotFound(repository.delete(id) != 0, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must be not null");
        restaurant.setName(restaurant.getName().toLowerCase());
        return repository.save(restaurant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Restaurant> findByName(String name, Pageable pageable) {
        return repository.findByName(name, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Restaurant findByMenu(int menuId) {
        return checkNotFoundWithId(repository.findByMenuId(menuId), menuId);
    }
}
