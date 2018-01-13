package ru.aovechnikov.voting.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 *Utility class for working with encoding.
 *
 * @author - A.Ovechnikov
 * @date - 13.01.2018
 */

public class PasswordUtil {

private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * @return {@link BCryptPasswordEncoder}
     */
    public static PasswordEncoder getPasswordEncoder() {
        return PASSWORD_ENCODER;
    }

    /**
     * Encode the raw password.
     *
     * @param newPassword password to encode
     * @return encoded password.
     */
    public static String encode(String newPassword){
        if (!StringUtils.hasText(newPassword)){
            return null;
        }
        return PASSWORD_ENCODER.encode(newPassword);
    }
}
