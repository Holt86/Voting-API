package ru.aovechnikov.voting.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aovechnikov.voting.model.Role;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.to.UserTo;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static ru.aovechnikov.voting.testutil.TestUtil.PAGEABLE;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.*;
import static ru.aovechnikov.voting.util.UserUtil.asTo;

/**
 * For testing {@link UserService}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class UserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void testFindById() throws Exception {
        MATCHER_FOR_USER.assertEquals(USER1, service.findById(USER1_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdNotFound() throws Exception {
        service.findById(99);
    }

    @Test
    public void testFindAll() throws Exception {
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER1, USER2, ADMIN), service.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(USER1_ID);
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER2, ADMIN), service.findAll(PAGEABLE).getContent());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        service.findById(99);
    }

    @Test
    public void testUpdate() throws Exception {
        User updated = getUpdatedUser();
        service.save(updated);
        MATCHER_FOR_USER.assertEquals(updated, service.findById(updated.getId()));
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER1, updated, ADMIN), service.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testUpdateUserTo() throws Exception {
        User updated = getUpdatedUser();
        UserTo updatedTo = asTo(updated);
        service.update(updatedTo);
        MATCHER_FOR_USER.assertEquals(updated, service.findById(updated.getId()));
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER1, updated, ADMIN), service.findAll(PAGEABLE).getContent());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateUserToNotFound() throws Exception {
        User user = getCreatedUser();
        user.setId(125);
        service.update(asTo(user));
    }

    @Test
    public void testCreate() throws Exception {
        User created = getCreatedUser();
        service.save(created);
        MATCHER_FOR_USER.assertEquals(created, service.findById(created.getId()));
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER1, USER2, ADMIN, created), service.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testFindByEmail() throws Exception {
        MATCHER_FOR_USER.assertEquals(ADMIN, service.findByEmail("admin@mail.ru"));
    }

    @Test(expected = NotFoundException.class)
    public void testFindByEmailNotFound() throws Exception {
        service.findByEmail("admin@yandex.ru");
    }

    @Test
    public void validationException() throws Exception {
        validateRootCause(() -> service.save(new User(null, "  ", "invalid@yandex.ru", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.save(new User(null, "User", "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.save(new User(null, "User", "invalid@yandex.ru", "  ", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.save(new User(null, "User", "mail@yandex.ru", "password", new Date(), true, Collections.emptySet())), ConstraintViolationException.class);
    }

    @Test
    public void testEnable() throws Exception {
        User disable = new User(USER1);
        disable.setEnabled(false);
        service.enable(USER1_ID, false);
        MATCHER_FOR_USER.assertEquals(disable, service.findById(USER1_ID));
    }
}