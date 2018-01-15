package ru.aovechnikov.voting.web.halresource;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.ResourceSupport;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.web.json.DoubleToPriceViewSerializer;

/**
 * DTO class for creating resource representation for {@link Dish}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
public class DishResource extends ResourceSupport {

    private String name;

    @JsonSerialize(using = DoubleToPriceViewSerializer.class)
    private double price;

    public DishResource() {
    }

    public DishResource(Dish entity) {
        this.name = entity.getName();
        this.price = entity.getPrice();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "DishResource{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
