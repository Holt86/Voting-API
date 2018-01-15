package ru.aovechnikov.voting.web.halresource;

import org.springframework.hateoas.ResourceSupport;
import ru.aovechnikov.voting.model.Menu;

import java.time.LocalDate;

/**
 * DTO class for creating resource representation for {@link Menu}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
public class MenuResource extends ResourceSupport{

    private LocalDate date;

    public MenuResource() {
    }

    public MenuResource(Menu menu) {
        this.date = menu.getDate();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MenuResource{" +
                "date=" + date +
                '}';
    }
}