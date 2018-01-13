package ru.aovechnikov.voting.to;


import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DishTo extends HasIdTo {

    @NotBlank
    private String name;

    @Range(min = 0, max = Integer.MAX_VALUE)
    @NotNull
    private Double price;

    public DishTo() {
    }

    public DishTo(Integer id, String name, Double price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "DishTo{" +
                "id=" + getId() + '\'' +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
