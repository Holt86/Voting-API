package ru.aovechnikov.voting.web.servlet.halresource;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.ResourceSupport;
import ru.aovechnikov.voting.model.Role;
import ru.aovechnikov.voting.model.User;

import java.util.Date;
import java.util.Set;

import static ru.aovechnikov.voting.util.DateTimeUtil.DATE_TIME_PATTERN;

/**
 * DTO class for creating resource representation for {@link User}.
 *
 * @author - A.Ovechnikov
 * @date - 14.01.2018
 */
public class UserResource extends ResourceSupport {

    private String name;

    private String email;

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private Date registered;

    private boolean enabled;

    private Set<Role> roles;

    public UserResource() {
    }

    public UserResource(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.registered = user.getRegistered();
        this.enabled = user.isEnabled();
        this.roles = user.getRoles();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserResource{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", registered=" + registered +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }
}