package ru.aovechnikov.voting.testutil.testdata;

import ru.aovechnikov.voting.matcher.BeanMatcher;
import ru.aovechnikov.voting.model.User;

import java.util.Objects;

import static ru.aovechnikov.voting.model.AbstractBaseEntity.START_SEQ;
import static ru.aovechnikov.voting.model.Role.ROLE_ADMIN;
import static ru.aovechnikov.voting.model.Role.ROLE_USER;

/**
 * Test data for {@link User}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class UserTestData {

    public static final int ID_NOT_FOUND = 99;

    public static final BeanMatcher<User> MATCHER_FOR_USER = new BeanMatcher<User>(
            (expected, actual) -> expected == actual ||
                    (
                            Objects.equals(expected.getId(), (actual.getId())) &&
                            Objects.equals(expected.getName(), actual.getName()) &&
                            Objects.equals(expected.getEmail(), actual.getEmail()) &&
                            Objects.equals(expected.isEnabled(), actual.isEnabled()) &&
                            Objects.equals(expected.getRoles(), actual.getRoles())
                    )
    );

    public static final int USER1_ID = START_SEQ;
    public static final int USER2_ID = START_SEQ + 1;
    public static final int ADMIN_ID = START_SEQ + 2;

    public static final User USER1 = new User(USER1_ID, "User1", "user1@yandex.ru", "password1", ROLE_USER);
    public static final User USER2 = new User(USER2_ID, "User2", "user2@yandex.ru", "password2", ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@mail.ru", "admin", ROLE_ADMIN, ROLE_USER);

    public static User getUpdatedUser(){
        return new User(USER2_ID, "Обновленный", "updated@yandex.ru", "updated", ROLE_USER);
    }

    public static User getCreatedUser(){
        return new User(null, "Новый", "new@yandex.ru", "newnew", ROLE_USER);
    }
}
