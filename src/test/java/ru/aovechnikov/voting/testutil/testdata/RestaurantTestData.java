package ru.aovechnikov.voting.testutil.testdata;


import ru.aovechnikov.voting.matcher.BeanMatcher;
import ru.aovechnikov.voting.model.Restaurant;

import java.util.Objects;

import static ru.aovechnikov.voting.model.AbstractBaseEntity.START_SEQ;

/**
 * Test data for {@link Restaurant}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class RestaurantTestData {

    public static final BeanMatcher<Restaurant> MATCHER_FOR_RESTAURANT = new BeanMatcher<Restaurant>((
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId()) &&
                            Objects.equals(expected.getName(), actual.getName())))
    );

    public static final int MAMA_ROMA_ID = START_SEQ + 3;
    public static final int GRILL_MASTER_ID = START_SEQ + 4;
    public static final int CAROLS_ID = START_SEQ + 5;

    public static final Restaurant MAMA_ROMA = new Restaurant(MAMA_ROMA_ID, "mama roma");
    public static final Restaurant GRILL_MASTER = new Restaurant(GRILL_MASTER_ID, "grill master");
    public static final Restaurant CAROLS = new Restaurant(CAROLS_ID, "carols");

    public static Restaurant getUpdatedRestaurant() {
        return new Restaurant(MAMA_ROMA_ID, "обновленный");
    }

    public static Restaurant getCreatedRestaurant() {
        return new Restaurant(null, "новый");
    }
}
