package ru.aovechnikov.voting.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

import static ru.aovechnikov.voting.testutil.TestUtil.PAGEABLE;
import static ru.aovechnikov.voting.testutil.testdata.DateTestData.DATE_1;
import static ru.aovechnikov.voting.testutil.testdata.DishTestData.DISH_1_ID_MENU_1;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.*;
import static ru.aovechnikov.voting.testutil.testdata.RestaurantTestData.CAROLS_ID;
import static ru.aovechnikov.voting.testutil.testdata.RestaurantTestData.MAMA_ROMA_ID;

/**
 * For testing {@link MenuService}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class MenuServiceImplTest extends AbstractServiceTest {

    @Autowired
    private MenuService service;

    @Test
    public void testFindById() throws Exception {
        MATCHER_FOR_MENU.assertEquals(MENU_3, service.findById(MENU_3_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdNotFound() throws Exception {
        service.findById(99);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(MENU_1_ID);
        MATCHER_FOR_MENU.assertCollectionsEquals(Arrays.asList(MENU_3), service.findByRestaurantId(MAMA_ROMA_ID, PAGEABLE).getContent());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        service.delete(99);
    }

    @Test
    public void testCreate() throws Exception {
        Menu created = getCreatedMenu();
        service.create(created, CAROLS_ID);
        MATCHER_FOR_MENU.assertCollectionsEquals(Arrays.asList(MENU_1, MENU_2, created), service.findByDate(DATE_1, PAGEABLE).getContent());
    }

    @Test
    public void testUpdate() throws Exception {
        Menu updated = getUpdatedMenu();
        service.update(updated);
        MATCHER_FOR_MENU.assertCollectionsEquals(Arrays.asList(updated, MENU_3), service.findByRestaurantId(MAMA_ROMA_ID, PAGEABLE).getContent());
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundUpdate() throws Exception {
        Menu updated = getUpdatedMenu();
        updated.setId(99);
        service.update(updated);
    }

    @Test
    public void testFindByRestaurantId() throws Exception {
        MATCHER_FOR_MENU.assertCollectionsEquals(Arrays.asList(MENU_1, MENU_3), service.findByRestaurantId(MAMA_ROMA_ID, PAGEABLE).getContent());
    }

    @Test
    public void testFindByDate() throws Exception {
        MATCHER_FOR_MENU.assertCollectionsEquals(Arrays.asList(MENU_1, MENU_2), service.findByDate(DATE_1, PAGEABLE).getContent());
    }

    @Test
    public void validationException() throws Exception {
        validateRootCause(() -> service.create(new Menu(null, null, null), MAMA_ROMA_ID), ConstraintViolationException.class);
    }

    @Test
    public void testFindMenuByDish() throws Exception {
        MATCHER_FOR_MENU.assertEquals(MENU_1, service.findMenuByDish(DISH_1_ID_MENU_1));
    }

    @Test(expected = AccessDeniedException.class)
    public void testDeleteAccessDenied() throws Exception {
        configureAuthentication("ROLE_USER");
        service.delete(MENU_1_ID);
    }

    @Test(expected = AccessDeniedException.class)
    public void testCreateAccessDenied() throws Exception {
        configureAuthentication("ROLE_USER");
        Menu created = getCreatedMenu();
        service.create(created, CAROLS_ID);
    }

    @Test(expected = AccessDeniedException.class)
    public void testUpdateAccessDenied() throws Exception {
        configureAuthentication("ROLE_USER");
        Menu updated = getUpdatedMenu();
        service.update(updated);
    }
}