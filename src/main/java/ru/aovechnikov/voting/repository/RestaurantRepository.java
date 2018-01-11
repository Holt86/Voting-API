package ru.aovechnikov.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aovechnikov.voting.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
