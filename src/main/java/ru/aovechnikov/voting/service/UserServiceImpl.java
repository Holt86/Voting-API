package ru.aovechnikov.voting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.to.UserTo;
import ru.aovechnikov.voting.util.exception.NotFoundException;

@Service
public class UserServiceImpl implements UserService{

    @Override
    public User findById(int id) throws NotFoundException {
        return null;
    }

    @Override
    public void delete(int id) throws NotFoundException {
    }

    @Override
    public User update(User user) throws NotFoundException {
        return null;
    }

    @Override
    public void update(UserTo userTo) throws NotFoundException {
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        return null;
    }

    @Override
    public void enable(int id, boolean enable) {
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }
}
