package ru.aovechnikov.voting.model;

import java.time.LocalDate;

/**
 * Simple entity representing an voting.
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */
public class Vote extends AbstractBaseEntity {

    private LocalDate date;

    private User user;

    private Menu menu;

    public Vote() {
    }

    public Vote(Integer id, LocalDate date, User user, Menu menu) {
        super(id);
        this.date = date;
        this.user = user;
        this.menu = menu;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + getId() +
                ", date=" + date +
                ", user=" + user +
                ", menu=" + menu +
                '}';
    }
}
