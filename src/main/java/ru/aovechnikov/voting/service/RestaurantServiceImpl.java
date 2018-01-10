package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.aovechnikov.voting.model.Restaurant;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Override
    public Restaurant findById(int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return null;
    }

    @Override
    public Page<Restaurant> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Restaurant findByMenu(int menuId) {
        return null;
    }
}
