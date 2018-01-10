package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.to.DishTo;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.time.LocalDate;


public interface DishService {

    Dish findById(int id) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    Dish create(DishTo dish, int menuId);

    Dish update(DishTo dishTo);

    Page<Dish> findByMenu(int menuId, Pageable pageable);

    Page<Dish> findByDate(LocalDate date, Pageable pageable);
}
