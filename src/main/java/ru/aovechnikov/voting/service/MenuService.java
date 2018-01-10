package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.time.LocalDate;

public interface MenuService {

    Menu findById(int id) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    Menu create(Menu menu, int restaurantId);

    Menu update(Menu menu);

    Page<Menu> findByRestaurantId(int restaurantId, Pageable pageable);

    Page<Menu> findByDate(LocalDate date, Pageable pageable);

    Menu findMenuByDish(int dishId);
}
