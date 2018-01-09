package ru.aovechnikov.voting.model;

/**
 * Simple entity representing an restaurant
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */
public class Restaurant extends AbstractNamedEntity {

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + getId() +
                ", name=" + getName() + '}';
    }
}
