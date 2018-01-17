package ru.aovechnikov.voting.web.servlet.controllers;

import org.junit.Test;
import org.springframework.hateoas.MediaTypes;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * For testing {@link RootController}
 *
 * @author - A.Ovechnikov
 * @date - 17.01.2018
 */
public class RootControllerTest extends AbstractControllerTest {

    @Test
    public void testRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("rest"));
    }

    @Test
    public void testRootRest() throws Exception {
        mockMvc.perform(get("/rest"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._links.users.href", endsWith(AdminUserController.REST_URL)))
                .andExpect(jsonPath("$._links.profile.href", endsWith(ProfileUserController.REST_URL)))
                .andExpect(jsonPath("$._links.restaurants.href", endsWith(RestaurantController.REST_URL)))
                .andExpect(jsonPath("$._links.menus.href", endsWith(MenuController.REST_URL)))
                .andExpect(jsonPath("$._links.dishes.href", endsWith(DishController.REST_URL)))
                .andExpect(jsonPath("$._links.vote.href", endsWith(VoteController.REST_URL + "{?date}")))
                .andExpect(jsonPath("$._links.result.href", endsWith(VoteController.REST_URL + "/result{?date}")));
    }
}