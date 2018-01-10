package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.aovechnikov.voting.model.Restaurant;


public interface RestaurantService {

    Restaurant findById(int id);

    void delete(int id);

    Restaurant save(Restaurant restaurant);

    Page<Restaurant> findByName(String name, Pageable pageable);

    Restaurant findByMenu(int menuId);
}
