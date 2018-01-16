package ru.aovechnikov.voting.web.servlet.controllers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.service.DishService;
import ru.aovechnikov.voting.service.MenuService;

import java.util.Arrays;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aovechnikov.voting.testutil.TestUtil.*;
import static ru.aovechnikov.voting.testutil.VerifyJsonPathUtil.*;
import static ru.aovechnikov.voting.testutil.testdata.DateTestData.DATE_1;
import static ru.aovechnikov.voting.testutil.testdata.DishTestData.DISH_1_MENU_1;
import static ru.aovechnikov.voting.testutil.testdata.DishTestData.MATCHER_FOR_DISH;
import static ru.aovechnikov.voting.testutil.testdata.DishTestData.getCreatedDish;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.*;
import static ru.aovechnikov.voting.testutil.testdata.RestaurantTestData.MAMA_ROMA;
import static ru.aovechnikov.voting.testutil.testdata.RestaurantTestData.MAMA_ROMA_ID;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.ADMIN;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.ID_NOT_FOUND;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.USER1;
import static ru.aovechnikov.voting.util.exception.ErrorType.*;
import static ru.aovechnikov.voting.web.servlet.controllers.MenuController.MENU_URL;

/**
 * For testing {@link MenuController}.
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public class MenuControllerTest extends AbstractControllerTest {

    private static final String URL_TEST = MENU_URL + '/';

    @Autowired
    private MenuService menuService;

    @Autowired
    private DishService dishService;

    @Test
    public void testFindById() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + MENU_1_ID)
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForMenu(actions, MENU_1);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL_TEST + MENU_1_ID)
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print());
        MATCHER_FOR_MENU.assertCollectionsEquals(Arrays.asList(MENU_3), menuService.findByRestaurantId(MAMA_ROMA_ID, PAGEABLE).getContent());
    }

    @Test
    public void testUpdate() throws Exception {
        Menu updated = getUpdatedMenu();
        ResultActions actions = mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForMenu(actions, updated);
        MATCHER_FOR_MENU.assertEquals(updated, menuService.findById(updated.getId()));
    }

    @Test
    public void testFindByDate() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + "/by-date?date=" + DATE_1 + "&page=0&size=2&sort=id")
                .with(httpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._embedded.menuResourceList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.menuResourceList[0].date").value(MENU_1.getDate().toString()))
                .andExpect(jsonPath("$._embedded.menuResourceList[0]._links.self.href", endsWith("/menus/" + MENU_1_ID)))
                .andExpect(jsonPath("$._embedded.menuResourceList[1].date").value(MENU_2.getDate().toString()))
                .andExpect(jsonPath("$._embedded.menuResourceList[1]._links.self.href", endsWith("/menus/" + MENU_2_ID)));

        verifyJsonForPageParam(actions, 2, 2, 1, 0);
    }

    @Test
    public void testFindByRestaurant() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + "/by-restaurant?id=" + MAMA_ROMA_ID + "&page=0&size=2&sort=date,desc")
                .with(httpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._embedded.menuResourceList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.menuResourceList[0].date").value(MENU_3.getDate().toString()))
                .andExpect(jsonPath("$._embedded.menuResourceList[0]._links.self.href", endsWith("/menus/" + MENU_3_ID)))
                .andExpect(jsonPath("$._embedded.menuResourceList[1].date").value(MENU_1.getDate().toString()))
                .andExpect(jsonPath("$._embedded.menuResourceList[1]._links.self.href", endsWith("/menus/" + MENU_1_ID)));

        verifyJsonForPageParam(actions, 2, 2, 1, 0);
    }

    @Test
    public void testFindRestaurantForMenu() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + MENU_1_ID + "/restaurant")
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForRestaurant(actions, MAMA_ROMA);
    }

    @Test
    public void testCreateDishForMenu() throws Exception {
        Dish created = getCreatedDish();
        ResultActions actions = mockMvc.perform(post(URL_TEST + MENU_1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(created))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        created.setId(getIdFromLink(actions, "$._links.self.href"));
        MATCHER_FOR_DISH.assertEquals(created, dishService.findById(created.getId()));
        verifyJsonForDish(actions, created);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testDuplicateCreateDish() throws Exception {
        Dish created = getCreatedDish();
        created.setName(DISH_1_MENU_1.getName());
        mockMvc.perform(post(URL_TEST + MENU_1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(created))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(DATA_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testUpdateMenuConsistentId() throws Exception {
        Menu updated = getUpdatedMenu();
        mockMvc.perform(put(URL_TEST + MENU_2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value(APP_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testValidationMenuUpdate() throws Exception {
        Menu updated = getUpdatedMenu();
        updated.setDate(null);
        mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get(URL_TEST + ID_NOT_FOUND)
                .with(httpBasic(USER1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(DATA_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(URL_TEST + ID_NOT_FOUND)
                .with(httpBasic(ADMIN)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(DATA_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        Menu updated = getUpdatedMenu();
        updated.setId(ID_NOT_FOUND);
        mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(DATA_NOT_FOUND.name()))
                .andDo(print());
    }
}