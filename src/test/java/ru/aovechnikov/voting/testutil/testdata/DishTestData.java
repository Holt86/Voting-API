package ru.aovechnikov.voting.testutil.testdata;

import ru.aovechnikov.voting.matcher.BeanMatcher;
import ru.aovechnikov.voting.model.Dish;

import java.util.Objects;

import static ru.aovechnikov.voting.model.AbstractBaseEntity.START_SEQ;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_1;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_2;

/**
 * Test data for {@link Dish}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class DishTestData {

    public static final BeanMatcher<Dish> MATCHER_FOR_DISH = new BeanMatcher<Dish>((
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId()) &&
                            Objects.equals(expected.getName(), actual.getName()) &&
                            Objects.equals(expected.getPrice(), actual.getPrice())))
    );

    public static final Integer DISH_1_ID_MENU_1 = START_SEQ + 10;
    public static final Integer DISH_2_ID_MENU_1 = START_SEQ + 11;
    public static final Integer DISH_3_ID_MENU_1 = START_SEQ + 12;
    public static final Integer DISH_1_ID_MENU_2 = START_SEQ + 15;
    public static final Integer DISH_2_ID_MENU_2 = START_SEQ + 16;

    public static final Dish DISH_1_MENU_1 = new Dish(DISH_1_ID_MENU_1, "пицца паперони", 350.00, MENU_1);
    public static final Dish DISH_2_MENU_1 = new Dish(DISH_2_ID_MENU_1, "лазанья с ветчиной", 460.50, MENU_1);
    public static final Dish DISH_3_MENU_1 = new Dish(DISH_3_ID_MENU_1, "спагетини", 470.37, MENU_1);
    public static final Dish DISH_1_MENU_2 = new Dish(DISH_1_ID_MENU_2, "ребрышки барбекю", 580.00, MENU_2);
    public static final Dish DISH_2_MENU_2 = new Dish(DISH_2_ID_MENU_2, "шашлык бараний", 570.25, MENU_2);

    public static Dish getUpdatedDish() {
        return new Dish(DISH_1_ID_MENU_1, "обновленная", 420.25, null);
    }

    public static Dish getCreatedDish() {
        return new Dish(null, "новая", 590.50, null);
    }
}
