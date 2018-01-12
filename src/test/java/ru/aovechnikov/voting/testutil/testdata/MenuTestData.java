package ru.aovechnikov.voting.testutil.testdata;


import ru.aovechnikov.voting.matcher.BeanMatcher;
import ru.aovechnikov.voting.model.Menu;

import java.time.LocalDate;
import java.util.Objects;

import static ru.aovechnikov.voting.model.AbstractBaseEntity.START_SEQ;
import static ru.aovechnikov.voting.testutil.testdata.DateTestData.DATE_1;
import static ru.aovechnikov.voting.testutil.testdata.DateTestData.DATE_2;
import static ru.aovechnikov.voting.testutil.testdata.RestaurantTestData.*;

/**
 * Test data for {@link Menu}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class MenuTestData {

    public static final BeanMatcher<Menu> MATCHER_FOR_MENU = new BeanMatcher<Menu>((
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId()) &&
                            Objects.equals(expected.getDate(), actual.getDate())))

    );

    public static final int MENU_1_ID = START_SEQ + 6;
    public static final int MENU_2_ID = START_SEQ + 7;
    public static final int MENU_3_ID = START_SEQ + 8;
    public static final int MENU_4_ID = START_SEQ + 9;

    public static final Menu MENU_1 = new Menu(MENU_1_ID, DATE_1, MAMA_ROMA);
    public static final Menu MENU_2 = new Menu(MENU_2_ID, DATE_1, GRILL_MASTER);
    public static final Menu MENU_3 = new Menu(MENU_3_ID, DATE_2, MAMA_ROMA);
    public static final Menu MENU_4 = new Menu(MENU_4_ID, DATE_2, GRILL_MASTER);

    public static Menu getCreatedMenu(){
        return new Menu(null, DATE_1, CAROLS);
    }

    public static Menu getUpdatedMenu(){
        return new Menu(MENU_1_ID, LocalDate.of(2018, 01, 03), null);
    }
}
