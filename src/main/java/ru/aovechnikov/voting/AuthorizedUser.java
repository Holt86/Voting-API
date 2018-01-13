package ru.aovechnikov.voting;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.to.UserTo;
import ru.aovechnikov.voting.util.UserUtil;

/**
 * Models user information retrieved by a {@link UserDetailsService}.
 *
 * @author - A.Ovechnikov
 * @date - 13.01.2018
 */

public class AuthorizedUser extends org.springframework.security.core.userdetails.User{

    static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.userTo = UserUtil.asTo(user);
    }

    public int getId(){
        return userTo.getId();
    }
}
