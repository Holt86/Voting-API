package ru.aovechnikov.voting.model;

import java.time.LocalDate;

/**
 * Simple entity representing an menu.
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */
public class Menu extends AbstractBaseEntity {

    private LocalDate date;

    private Restaurant restaurant;

    public Menu() {
    }

    public Menu(Integer id, LocalDate date, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + getId() +
                ", date=" + date +
                ", restaurant=" + restaurant +
                '}';
    }
}
