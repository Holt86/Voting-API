package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.time.LocalDate;

@Service
public class MenuServiceImpl implements MenuService {


    @Override
    public Menu findById(int id) throws NotFoundException {
        return null;
    }

    @Override
    public void delete(int id) throws NotFoundException {
    }

    @Override
    public Menu create(Menu menu, int restaurantId) {
        return null;
    }

    @Override
    public Menu update(Menu menu) {
        return null;
    }

    @Override
    public Page<Menu> findByRestaurantId(int restaurantId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Menu> findByDate(LocalDate date, Pageable pageable) {
        return null;
    }

    @Override
    public Menu findMenuByDish(int dishId) {
        return null;
    }
}
