package ru.aovechnikov.voting.web.halresource;


import org.springframework.hateoas.ResourceSupport;
import ru.aovechnikov.voting.model.Restaurant;

/**
 * DTO class for creating resource representation for {@link Restaurant}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
public class RestaurantResource extends ResourceSupport {

    private String name;

    public RestaurantResource() {
    }

    public RestaurantResource(Restaurant restaurant) {
        this.name = restaurant.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RestaurantResource{" +
                ", name='" + name + '\'' +
                '}';
    }
}
