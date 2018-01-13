package ru.aovechnikov.voting.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents an authority granted as a <code>String</code> to an {@link Authentication} object.
 *
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */
public enum Role implements GrantedAuthority {

    ROLE_USER,
    ROLE_ADMIN;

    /**
     * @return a representation of the granted authority.
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
