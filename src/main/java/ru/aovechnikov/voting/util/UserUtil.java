package ru.aovechnikov.voting.util;

import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.to.UserTo;

/**
 * Utility class for working with user data.
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class UserUtil {

    public static User updateFromTo(User user, UserTo userTo){
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    /**
     * Encodes the user's password before saving it to the database.
     *
     * @param user unprepared user
     * @return {@link User} ready to save the user
     */
    public static User prepareToSave(User user){
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
