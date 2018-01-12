package ru.aovechnikov.voting.util;

import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.to.UserTo;

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

    public static User prepareToSave(User user){
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
