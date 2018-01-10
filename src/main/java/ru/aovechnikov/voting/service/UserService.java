package ru.aovechnikov.voting.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.to.UserTo;
import ru.aovechnikov.voting.util.exception.NotFoundException;

public interface UserService {

    User findById(int id) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    User update(User user) throws NotFoundException;

    void update(UserTo userTo) throws NotFoundException;

    User create(User user);

    User findByEmail(String email) throws NotFoundException;

    void enable(int id, boolean enable);

    Page<User> findAll(Pageable pageable);
}
