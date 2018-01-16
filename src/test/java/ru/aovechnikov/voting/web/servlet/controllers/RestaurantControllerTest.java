package ru.aovechnikov.voting.web.servlet.controllers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.Restaurant;
import ru.aovechnikov.voting.service.MenuService;
import ru.aovechnikov.voting.service.RestaurantService;
import ru.aovechnikov.voting.testutil.TestUtil;
import ru.aovechnikov.voting.testutil.VerifyJsonPathUtil;

import java.util.Arrays;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aovechnikov.voting.testutil.TestUtil.getIdFromLink;
import static ru.aovechnikov.voting.testutil.TestUtil.httpBasic;
import static ru.aovechnikov.voting.testutil.VerifyJsonPathUtil.verifyJsonForRestaurant;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MATCHER_FOR_MENU;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_1;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.getCreatedMenu;
import static ru.aovechnikov.voting.testutil.testdata.RestaurantTestData.*;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.ADMIN;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.ID_NOT_FOUND;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.USER1;
import static ru.aovechnikov.voting.util.exception.ErrorType.*;
import static ru.aovechnikov.voting.web.servlet.controllers.RestaurantController.URL_REST;

/**
 * For testing {@link RestaurantController}
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public class RestaurantControllerTest extends AbstractControllerTest {

    private static final String URL_TEST = URL_REST + '/';//

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @Test
    public void testFindWithEmptyName() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + "by-name" + "?page=0&size=2&sort=name")
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._embedded.restaurantResourceList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.restaurantResourceList[0].name").value(CAROLS.getName()))
                .andExpect(jsonPath("$._embedded.restaurantResourceList[0]._links.self.href", endsWith("/restaurants/" + CAROLS_ID)))
                .andExpect(jsonPath("$._embedded.restaurantResourceList[1].name").value(GRILL_MASTER.getName()))
                .andExpect(jsonPath("$._embedded.restaurantResourceList[1]._links.self.href", endsWith("/restaurants/" + GRILL_MASTER_ID)));

        VerifyJsonPathUtil.verifyJsonForPageParam(actions, 2, 3, 2, 0);
    }

    @Test
    public void testFindByName() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + "by-name?name=" + MAMA_ROMA.getName().substring(5))
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._embedded.restaurantResourceList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.restaurantResourceList[0].name").value(MAMA_ROMA.getName()))
                .andExpect(jsonPath("$._embedded.restaurantResourceList[0]._links.self.href", endsWith("/restaurants/" + MAMA_ROMA_ID)));

        VerifyJsonPathUtil.verifyJsonForPageParam(actions, 20, 1, 1, 0);
    }

    @Test
    public void testFindById() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + MAMA_ROMA_ID)
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForRestaurant(actions, MAMA_ROMA);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL_TEST + MAMA_ROMA_ID)
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print());
        MATCHER_FOR_RESTAURANT.assertCollectionsEquals(Arrays.asList(GRILL_MASTER, CAROLS), restaurantService.findByName("", TestUtil.PAGEABLE).getContent());
    }

    @Test
    public void testCreate() throws Exception {
        Restaurant created = getCreatedRestaurant();
        ResultActions actions = mockMvc.perform(post(URL_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(created))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        created.setId(getIdFromLink(actions, "$._links.self.href"));
        verifyJsonForRestaurant(actions, created);
        MATCHER_FOR_RESTAURANT.assertEquals(created, restaurantService.findById(created.getId()));
    }

    @Test
    public void testUpdate() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        ResultActions actions = mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForRestaurant(actions, updated);
        MATCHER_FOR_RESTAURANT.assertEquals(updated, restaurantService.findById(updated.getId()));
    }

    @Test
    public void testCreateMenuForRestaurants() throws Exception {
        Menu created = getCreatedMenu();
        ResultActions actions = mockMvc.perform(post(URL_TEST + MAMA_ROMA_ID + "/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(created))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        created.setId(getIdFromLink(actions, "$._links.self.href"));
        MATCHER_FOR_MENU.assertEquals(created, menuService.findById(created.getId()));
        VerifyJsonPathUtil.verifyJsonForMenu(actions, created);
    }

    @Test
    public void testNotFound() throws Exception {
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
    @Transactional(propagation = Propagation.NEVER)
    public void testDuplicateCreateRestaurant() throws Exception {
        Restaurant created = getCreatedRestaurant();
        created.setName(MAMA_ROMA.getName());
        mockMvc.perform(post(URL_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(created))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.type").value(DATA_ERROR.name()))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testDuplicateCreateMenu() throws Exception {
        Menu created = getCreatedMenu();
        created.setDate(MENU_1.getDate());
        mockMvc.perform(post(URL_TEST + MAMA_ROMA_ID + "/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(created))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());

    }

    @Test
    public void testUpdateConsistentId() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        mockMvc.perform(put(URL_TEST + GRILL_MASTER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.type").value(APP_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testValidationUpdate() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        updated.setName("");
        mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                .andDo(print());
    }

}