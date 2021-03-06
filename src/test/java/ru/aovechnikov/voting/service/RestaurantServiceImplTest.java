package ru.aovechnikov.voting.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

import static ru.aovechnikov.voting.testutil.TestUtil.PAGEABLE;
import static ru.aovechnikov.voting.testutil.testdata.RestaurantTestData.*;

/**
 * For testing {@link RestaurantService}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class RestaurantServiceImplTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    public void testFindById() throws Exception {
        MATCHER_FOR_RESTAURANT.assertEquals(MAMA_ROMA, service.findById(MAMA_ROMA_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdNotFound() throws Exception {
        service.findById(99);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(MAMA_ROMA_ID);
        MATCHER_FOR_RESTAURANT.assertCollectionsEquals(Arrays.asList(GRILL_MASTER, CAROLS), service.findByName("", PAGEABLE).getContent());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        service.delete(99);
    }

    @Test
    public void testUpdate() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        service.save(updated);
        MATCHER_FOR_RESTAURANT.assertEquals(updated, service.findById(updated.getId()));
        MATCHER_FOR_RESTAURANT.assertCollectionsEquals(Arrays.asList(updated, GRILL_MASTER, CAROLS), service.findByName("", PAGEABLE).getContent());
    }

    @Test
    public void testCreate() throws Exception {
        Restaurant created = getCreatedRestaurant();
        service.save(created);
        MATCHER_FOR_RESTAURANT.assertEquals(created, service.findById(created.getId()));
        MATCHER_FOR_RESTAURANT.assertCollectionsEquals(Arrays.asList(MAMA_ROMA, GRILL_MASTER, CAROLS, created), service.findByName("", PAGEABLE).getContent());
    }

    @Test
    public void testFindAll1() throws Exception {
        MATCHER_FOR_RESTAURANT.assertCollectionsEquals(Arrays.asList(MAMA_ROMA, GRILL_MASTER, CAROLS), service.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testFindByName() throws Exception {
        MATCHER_FOR_RESTAURANT.assertCollectionsEquals(Arrays.asList(MAMA_ROMA), service.findByName(MAMA_ROMA.getName(), PAGEABLE).getContent());
    }

    @Test
    public void testFindAll() throws Exception {
        MATCHER_FOR_RESTAURANT.assertCollectionsEquals(Arrays.asList(MAMA_ROMA, GRILL_MASTER, CAROLS), service.findByName("", PAGEABLE).getContent());
    }

    @Test
    public void validationException() throws Exception{
        validateRootCause(() -> service.save(new Restaurant(null, "")), ConstraintViolationException.class);
    }

    @Test(expected = AccessDeniedException.class)
    public void testDeleteAccessDenied() throws Exception {
        configureAuthentication("ROLE_USER");
        service.delete(MAMA_ROMA_ID);
    }

    @Test(expected = AccessDeniedException.class)
    public void testUpdateAccessDenied() throws Exception {
        configureAuthentication("ROLE_USER");
        Restaurant updated = getUpdatedRestaurant();
        service.save(updated);
    }
}