package ru.aovechnikov.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.repository.DishRepository;
import ru.aovechnikov.voting.repository.MenuRepository;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.time.LocalDate;

import static ru.aovechnikov.voting.util.ValidationUtil.checkNotFound;

/**
 * Implementation of the {@link DishService}
 *
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuRepository menuRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Dish findById(int id){
        return dishRepository.findById(id).orElseThrow(() ->
                new NotFoundException("id=" + id));
    }

    /**
     * {@inheritDoc}
     */
    @Secured("ROLE_ADMIN")
    @Override
    public void delete(int id){
        checkNotFound(dishRepository.delete(id) != 0, id);
    }

    /**
     * {@inheritDoc}
     */
    @Secured("ROLE_ADMIN")
    @Override
    public Dish create(Dish dish, int menuId) {
        Assert.notNull(dish, "dish must be not null");
        dish.setMenu(menuRepository.getOne(menuId));
        return dishRepository.save(dish);
    }

    /**
     * {@inheritDoc}
     */
    @Secured("ROLE_ADMIN")
    @Transactional
    @Override
    public Dish update(Dish dish) {
        Assert.notNull(dish, "dish must be not null");
        findById(dish.getId());
        return dishRepository.save(dish);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Dish> findByMenu(int menuId, Pageable pageable) {
        Assert.notNull(pageable, "pageable must be not null");
        return dishRepository.findByMenuId(menuId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Dish> findByDate(LocalDate date, Pageable pageable) {
        Assert.notNull(date, "date must be not null");
        Assert.notNull(pageable, "pageable must be not null");
        return dishRepository.findByDate(date, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Dish> findAll(Pageable pageable) {
        Assert.notNull(pageable, "pageable must be not null");
        return dishRepository.findAll(pageable);
    }
}
