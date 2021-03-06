package ru.aovechnikov.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.aovechnikov.voting.AuthorizedUser;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.repository.UserRepository;
import ru.aovechnikov.voting.to.UserTo;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import static ru.aovechnikov.voting.util.UserUtil.prepareToSave;
import static ru.aovechnikov.voting.util.UserUtil.updateFromTo;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNotFound;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNotFoundWithId;

/**
 * Implementation of the {@link UserService}
 *
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public User findById(int id) {
        return repository.findById(id).orElseThrow(() ->
                new NotFoundException("id=" + id));
    }

    @Override
    public void delete(int id) {
        checkNotFound(repository.delete(id) != 0, id);
    }

    @Transactional
    @Override
    public void update(UserTo userTo) {
        Assert.notNull(userTo, "user must be not null");
        User user = updateFromTo(findById(userTo.getId()), userTo);
        checkNotFoundWithId(repository.save(prepareToSave(user)), user.getId());
    }

    @Override
    public User save(User user) {
        Assert.notNull(user, "user must be not null");
        return repository.save(prepareToSave(user));
    }

    @Override
    public User findByEmail(String email) {
        Assert.notNull(email, "email must be not null");
        return repository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(String.format("email=", email)));
    }

    @Transactional
    @Override
    public void enable(int id, boolean enabled) {
        User user = findById(id);
        user.setEnabled(enabled);
        repository.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Assert.notNull(pageable, "pageable must be not null");
        return repository.findAll(pageable);
    }

    /**
     * @see UserDetailsService#loadUserByUsername(String)
     *
     * @param email identifying the user whose data is required
     * @return {@link AuthorizedUser} a fully populated user record
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        Assert.notNull(email, "email must be not null");
        User user = repository.findByEmail(email.toLowerCase()).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with %s not found", email)));
        return new AuthorizedUser(user);
    }
}
