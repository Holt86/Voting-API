package ru.aovechnikov.voting.testutil.testdata;

import ru.aovechnikov.voting.matcher.BeanMatcher;
import ru.aovechnikov.voting.model.Vote;

import java.util.Objects;

import static ru.aovechnikov.voting.model.AbstractBaseEntity.START_SEQ;
import static ru.aovechnikov.voting.testutil.testdata.DateTestData.DATE_2;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_3;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_4;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.USER1;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.USER2;

/**
 * Test data for {@link Vote}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class VoteTestData {

    public static final BeanMatcher<Vote> MATCHER_FOR_VOTE = new BeanMatcher<>((
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId()) &&
                            Objects.equals(expected.getDate(), actual.getDate()) &&
                            Objects.equals(expected.getMenu(), actual.getMenu())
                    ))
    );

    public static final int VOTE1_ID = START_SEQ + 22;

    public static final Vote VOTE1 = new Vote(VOTE1_ID, DATE_2, USER2, MENU_3);

    public static Vote getCreatedVote(){
        return new Vote(null, DATE_2, USER1, MENU_4);
    }

    public static Vote getUpdatedVote(){
        return new Vote(VOTE1_ID, DATE_2, USER2, MENU_4);
    }
}
