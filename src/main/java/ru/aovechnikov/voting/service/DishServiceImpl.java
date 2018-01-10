package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.to.DishTo;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.time.LocalDate;

@Service
public class DishServiceImpl implements DishService {

    @Override
    public Dish findById(int id) throws NotFoundException {
        return null;
    }

    @Override
    public void delete(int id) throws NotFoundException {
    }

    @Override
    public Dish create(DishTo dish, int menuId) {
        return null;
    }

    @Override
    public Dish update(DishTo dishTo) {
        return null;
    }

    @Override
    public Page<Dish> findByMenu(int menuId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Dish> findByDate(LocalDate date, Pageable pageable) {
        return null;
    }
}
