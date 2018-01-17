package ru.aovechnikov.voting.testutil.testdata;

import ru.aovechnikov.voting.matcher.BeanMatcher;
import ru.aovechnikov.voting.model.Dish;

import java.util.Objects;

import static ru.aovechnikov.voting.model.AbstractBaseEntity.START_SEQ;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.*;

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
    public static final Integer DISH_1_ID_MENU_3 = START_SEQ + 13;
    public static final Integer DISH_2_ID_MENU_3 = START_SEQ + 14;
    public static final Integer DISH_1_ID_MENU_2 = START_SEQ + 15;
    public static final Integer DISH_2_ID_MENU_2 = START_SEQ + 16;
    public static final Integer DISH_1_ID_MENU_4 = START_SEQ + 17;
    public static final Integer DISH_2_ID_MENU_4 = START_SEQ + 18;

    public static final Dish DISH_1_MENU_1 = new Dish(DISH_1_ID_MENU_1, "пицца паперони", 350.0, MENU_1);
    public static final Dish DISH_2_MENU_1 = new Dish(DISH_2_ID_MENU_1, "лазанья с ветчиной", 460.5, MENU_1);
    public static final Dish DISH_3_MENU_1 = new Dish(DISH_3_ID_MENU_1, "спагетини", 470.37, MENU_1);
    public static final Dish DISH_1_MENU_2 = new Dish(DISH_1_ID_MENU_2, "ребрышки барбекю", 580.0, MENU_2);
    public static final Dish DISH_2_MENU_2 = new Dish(DISH_2_ID_MENU_2, "шашлык бараний", 570.25, MENU_2);
    public static final Dish DISH_1_MENU_3 = new Dish(DISH_1_ID_MENU_3, "пицца салями", 370.0, MENU_3);
    public static final Dish DISH_2_MENU_3 = new Dish(DISH_2_ID_MENU_3, "паста с грибами", 480.2, MENU_3);
    public static final Dish DISH_1_MENU_4 = new Dish(DISH_1_ID_MENU_4, "форель гриль", 840.0, MENU_4);
    public static final Dish DISH_2_MENU_4 = new Dish(DISH_2_ID_MENU_4, "ребрышки барбекю", 585.31, MENU_4);

    public static Dish getUpdatedDish() {
        return new Dish(DISH_1_ID_MENU_1, "обновленная", 420.25, null);
    }

    public static Dish getCreatedDish() {
        return new Dish(null, "новая", 590.5, null);
    }
}
