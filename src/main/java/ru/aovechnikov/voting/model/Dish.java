package ru.aovechnikov.voting.model;

/**
 * Simple entity representing an dish.
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */
public class Dish extends AbstractNamedEntity {

    private Integer price;

    private Menu menu;

    public Dish() {
    }

    public Dish(Integer id, String name, Integer price, Menu menu) {
        super(id, name);
        this.price = price;
        this.menu = menu;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", price=" + price +
                '}';
    }
}

