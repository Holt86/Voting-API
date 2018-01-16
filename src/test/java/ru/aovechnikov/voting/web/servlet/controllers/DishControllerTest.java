package ru.aovechnikov.voting.web.servlet.controllers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.aovechnikov.voting.model.Dish;
import ru.aovechnikov.voting.service.DishService;

import java.util.Arrays;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aovechnikov.voting.testutil.TestUtil.PAGEABLE;
import static ru.aovechnikov.voting.testutil.TestUtil.httpBasic;
import static ru.aovechnikov.voting.testutil.VerifyJsonPathUtil.*;
import static ru.aovechnikov.voting.testutil.testdata.DateTestData.DATE_1;
import static ru.aovechnikov.voting.testutil.testdata.DishTestData.*;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_1;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_1_ID;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.ADMIN;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.ID_NOT_FOUND;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.USER1;
import static ru.aovechnikov.voting.util.exception.ErrorType.*;
import static ru.aovechnikov.voting.util.exception.ErrorType.NOT_AUTHENTICATION;
import static ru.aovechnikov.voting.web.servlet.controllers.DishController.URL_DISH;

/**
 * For testing {@link DishController}
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public class DishControllerTest extends AbstractControllerTest{

    private static final String URL_TEST = URL_DISH + '/';

    @Autowired
    private DishService dishService;

    @Test
    public void testFindById() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + DISH_1_ID_MENU_1)
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForDish(actions, DISH_1_MENU_1);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL_TEST + DISH_1_ID_MENU_1)
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print());
        MATCHER_FOR_DISH.assertCollectionsEquals(Arrays.asList(DISH_2_MENU_1, DISH_3_MENU_1), dishService.findByMenu(MENU_1_ID, PAGEABLE).getContent());
    }

    @Test
    public void testUpdate() throws Exception {
        Dish updated = getUpdatedDish();
        ResultActions actions = mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForDish(actions, updated);

        MATCHER_FOR_DISH.assertEquals(updated, dishService.findById(updated.getId()));
        MATCHER_FOR_DISH.assertCollectionsEquals(Arrays.asList(updated, DISH_2_MENU_1, DISH_3_MENU_1),
                dishService.findByMenu(MENU_1_ID, PAGEABLE).getContent());
    }

    @Test
    public void testFindByDate() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + "/by-date?date=" + DATE_1 + "&page=0&size=5&sort=id")
                .with(httpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._embedded.dishResourceList", hasSize(5)))
                .andExpect(jsonPath("$._embedded.dishResourceList[0].name").value(DISH_1_MENU_1.getName()))
                .andExpect(jsonPath("$._embedded.dishResourceList[0].price").value(DISH_1_MENU_1.getPrice()))
                .andExpect(jsonPath("$._embedded.dishResourceList[0]._links.self.href", endsWith("/dishes/" + DISH_1_ID_MENU_1)))
                .andExpect(jsonPath("$._embedded.dishResourceList[1].name").value(DISH_2_MENU_1.getName()))
                .andExpect(jsonPath("$._embedded.dishResourceList[1].price").value(DISH_2_MENU_1.getPrice()))
                .andExpect(jsonPath("$._embedded.dishResourceList[1]._links.self.href", endsWith("/dishes/" + DISH_2_ID_MENU_1)))
                .andExpect(jsonPath("$._embedded.dishResourceList[2].name").value(DISH_3_MENU_1.getName()))
                .andExpect(jsonPath("$._embedded.dishResourceList[2].price").value(DISH_3_MENU_1.getPrice()))
                .andExpect(jsonPath("$._embedded.dishResourceList[2]._links.self.href", endsWith("/dishes/" + DISH_3_ID_MENU_1)))
                .andExpect(jsonPath("$._embedded.dishResourceList[3].name").value(DISH_1_MENU_2.getName()))
                .andExpect(jsonPath("$._embedded.dishResourceList[3].price").value(DISH_1_MENU_2.getPrice()))
                .andExpect(jsonPath("$._embedded.dishResourceList[3]._links.self.href", endsWith("/dishes/" + DISH_1_ID_MENU_2)))
                .andExpect(jsonPath("$._embedded.dishResourceList[4].name").value(DISH_2_MENU_2.getName()))
                .andExpect(jsonPath("$._embedded.dishResourceList[4].price").value(DISH_2_MENU_2.getPrice()))
                .andExpect(jsonPath("$._embedded.dishResourceList[4]._links.self.href", endsWith("/dishes/" + DISH_2_ID_MENU_2)));

        verifyJsonForPageParam(actions, 5, 5, 1, 0);
    }

    @Test
    public void testFindByMenu() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + "/by-menu?id=" + MENU_1_ID + "&page=0&size=3&sort=id")
                .with(httpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._embedded.dishResourceList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.dishResourceList[0].name").value(DISH_1_MENU_1.getName()))
                .andExpect(jsonPath("$._embedded.dishResourceList[0].price").value(DISH_1_MENU_1.getPrice()))
                .andExpect(jsonPath("$._embedded.dishResourceList[0]._links.self.href", endsWith("/dishes/" + DISH_1_ID_MENU_1)))
                .andExpect(jsonPath("$._embedded.dishResourceList[1].name").value(DISH_2_MENU_1.getName()))
                .andExpect(jsonPath("$._embedded.dishResourceList[1].price").value(DISH_2_MENU_1.getPrice()))
                .andExpect(jsonPath("$._embedded.dishResourceList[1]._links.self.href", endsWith("/dishes/" + DISH_2_ID_MENU_1)))
                .andExpect(jsonPath("$._embedded.dishResourceList[2].name").value(DISH_3_MENU_1.getName()))
                .andExpect(jsonPath("$._embedded.dishResourceList[2].price").value(DISH_3_MENU_1.getPrice()))
                .andExpect(jsonPath("$._embedded.dishResourceList[2]._links.self.href", endsWith("/dishes/" + DISH_3_ID_MENU_1)));

        verifyJsonForPageParam(actions, 3, 3, 1, 0);
    }

    @Test
    public void testFindMenuForDish() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + DISH_1_ID_MENU_1 + "/menu")
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForMenu(actions, MENU_1);
    }

    @Test
    public void testUpdateDishConsistentId() throws Exception {
        Dish updated = getUpdatedDish();
        mockMvc.perform(put(URL_TEST + DISH_1_ID_MENU_2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value(APP_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testValidationDishUpdate() throws Exception {
        Dish updated = getUpdatedDish();
        updated.setName("");
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
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
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
        Dish updated = getUpdatedDish();
        updated.setId(ID_NOT_FOUND);
        mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(DATA_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    public void testForbidden() throws Exception {
        mockMvc.perform(delete(URL_TEST + DISH_1_ID_MENU_1)
                .with(httpBasic(USER1)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.type").value(ACCESS_DENIED.name()))
                .andDo(print());
    }

    @Test
    public void testUnAuthentication() throws Exception {
        mockMvc.perform(get(URL_TEST + DISH_1_ID_MENU_1))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.type").value(NOT_AUTHENTICATION.name()))
                .andDo(print());
    }
}