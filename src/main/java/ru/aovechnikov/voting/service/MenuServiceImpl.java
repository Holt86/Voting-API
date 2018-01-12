package ru.aovechnikov.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.repository.MenuRepository;
import ru.aovechnikov.voting.repository.RestaurantRepository;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.time.LocalDate;

import static ru.aovechnikov.voting.util.DateTimeUtil.getCurrentDateIfNull;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNotFound;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNotFoundWithId;

/**
 * Implementation of the {@link MenuService}
 *
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Menu findById(int id) throws NotFoundException {
        return (menuRepository.findById(id)).orElseThrow(() ->
                new NotFoundException("id=" + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFound(menuRepository.delete(id) != 0, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Menu create(Menu menu, int restaurantId) {
        Assert.notNull(menu, "menu must be not null");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        return menuRepository.save(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Menu update(Menu menu) {
        Assert.notNull(menu, "menu must be not null");
        findById(menu.getId());
        return menuRepository.save(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Menu> findByRestaurantId(int restaurantId, Pageable pageable) {
        return menuRepository.findByRestaurantId(restaurantId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Menu> findByDate(LocalDate date, Pageable pageable) {
        return menuRepository.findByDate(getCurrentDateIfNull(date), pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Menu findMenuByDish(int dishId) {
        return checkNotFoundWithId(menuRepository.findMenuByDish(dishId), dishId);
    }
}
