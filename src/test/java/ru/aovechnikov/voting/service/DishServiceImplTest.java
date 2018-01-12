package ru.aovechnikov.voting.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.util.exception.NotFoundException;

import java.util.Arrays;

import static ru.aovechnikov.voting.testutil.testdata.DateTestData.DATE_1;
import static ru.aovechnikov.voting.testutil.testdata.DishTestData.*;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_1_ID;

/**
 * For testing {@link DishService}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class DishServiceImplTest extends AbstractServiceTest {

    @Autowired
    private DishService service;

    @Test
    public void testFindById() throws Exception {
        MATCHER_FOR_DISH.assertEquals(DISH_1_MENU_1, service.findById(DISH_1_ID_MENU_1));
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdNotFound() throws Exception {
        service.findById(99);
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(DISH_1_ID_MENU_1);
        MATCHER_FOR_DISH.assertCollectionsEquals(Arrays.asList(DISH_2_MENU_1, DISH_3_MENU_1), service.findByMenu(MENU_1_ID, pageable).getContent());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        service.delete(99);
    }

    @Test
    public void testUpdate() throws Exception {
        Dish updated = getUpdatedDish();
        service.create(updated, MENU_1_ID);

        MATCHER_FOR_DISH.assertEquals(updated, service.findById(updated.getId()));
        MATCHER_FOR_DISH.assertCollectionsEquals(Arrays.asList(updated, DISH_2_MENU_1, DISH_3_MENU_1),
                service.findByMenu(MENU_1_ID, pageable).getContent());
    }

    @Test
    public void testCreate() throws Exception {
        Dish created = getCreatedDish();
        Dish returned = service.create(created, MENU_1_ID);
        created.setId(returned.getId());
        MATCHER_FOR_DISH.assertEquals(created, service.findById(created.getId()));
        MATCHER_FOR_DISH.assertCollectionsEquals(Arrays.asList(DISH_1_MENU_1, DISH_2_MENU_1, DISH_3_MENU_1, created),
                service.findByMenu(MENU_1_ID, pageable).getContent());
    }

    @Test
    public void testFindByDate() throws Exception {
    MATCHER_FOR_DISH.assertCollectionsEquals(
            Arrays.asList(DISH_1_MENU_1, DISH_2_MENU_1, DISH_3_MENU_1, DISH_1_MENU_2, DISH_2_MENU_2),
            service.findByDate(DATE_1, pageable).getContent());
    }

    @Test
    public void testFindByMenuId() throws Exception {
      MATCHER_FOR_DISH.assertCollectionsEquals(Arrays.asList(DISH_1_MENU_1, DISH_2_MENU_1, DISH_3_MENU_1),
              service.findByMenu(MENU_1_ID, pageable).getContent());
    }
}