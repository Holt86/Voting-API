package ru.aovechnikov.voting.testutil;


import org.springframework.test.web.servlet.ResultActions;
import ru.aovechnikov.voting.model.*;
import ru.aovechnikov.voting.util.DateTimeUtil;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Util class for verify HAL+Json in tests.
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */

public class VerifyJsonPathUtil {

    public static void verifyJsonForRestaurant(ResultActions actions, Restaurant restaurant) throws Exception {
        actions.andExpect(jsonPath("$.name").value(restaurant.getName()))
                .andExpect(jsonPath("$._links.self.href", endsWith("/restaurants/" + restaurant.getId())))
                .andExpect(jsonPath("$._links.restaurant.href", endsWith("/restaurants/" + restaurant.getId())))
                .andExpect(jsonPath("$._links.menus.href", endsWith("/menus/by-restaurant?id=" + restaurant.getId())))
                .andExpect(jsonPath("$._links.create-menu.href", endsWith("/restaurants/" + restaurant.getId() + "/menus")));
    }

    public static void verifyJsonForMenu(ResultActions actions, Menu menu) throws Exception {
        actions.andExpect(jsonPath("$.date").value(menu.getDate().toString()))
                .andExpect(jsonPath("$._links.self.href", endsWith("/menus/" + menu.getId())))
                .andExpect(jsonPath("$._links.menu.href", endsWith("/menus/" + menu.getId())))
                .andExpect(jsonPath("$._links.restaurant.href", endsWith("/menus/" + menu.getId() + "/restaurant")))
                .andExpect(jsonPath("$._links.dishes.href", endsWith("/dishes/by-menu?id=" + menu.getId())))
                .andExpect(jsonPath("$._links.create-dish.href", endsWith("/menus/" + menu.getId() + "/dishes")));
        if (menu.getDate().equals(DateTimeUtil.getCurrentDate()))
                actions.andExpect(jsonPath("$._links.voting-menu.href", endsWith("/voting/" + menu.getId())));
    }

    public static void verifyJsonForDish(ResultActions actions, Dish dish) throws Exception {
        actions.andExpect(jsonPath("$.name").value(dish.getName()))
                .andExpect(jsonPath("$.price").value(dish.getPrice()))
                .andExpect(jsonPath("$._links.self.href", endsWith("/dishes/" + dish.getId())))
                .andExpect(jsonPath("$._links.dish.href", endsWith("/dishes/" + dish.getId())))
                .andExpect(jsonPath("$._links.menu.href", endsWith("/dishes/" + dish.getId() + "/menu")));
    }

    public static void verifyJsonForVote(ResultActions actions, Vote vote) throws Exception {
        actions.andExpect(jsonPath("$.date").value(vote.getDate().toString()))
                .andExpect(jsonPath("$._links.self.href", endsWith("/voting?date=" + vote.getDate().toString())))
                .andExpect(jsonPath("$._links.menu.href", endsWith("/menus/" + vote.getMenu().getId())));
    }

    public static void verifyJsonForUser(ResultActions actions, User user, String... roles) throws Exception {
        actions.andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.enabled").value(user.isEnabled()))
                .andExpect(jsonPath("$.roles", hasSize(user.getRoles().size())))
                .andExpect(jsonPath("$.roles").value(containsInAnyOrder(roles)));
    }

    public static void verifyJsonLinksForAdminUser(ResultActions actions, User user) throws Exception{
        actions.andExpect(jsonPath("$._links.self.href", endsWith("/users/" + user.getId())))
                .andExpect(jsonPath("$._links.user.href", endsWith("/users/" + user.getId())))
                .andExpect(jsonPath("$._links.enabled.href", endsWith("/users/" + user.getId() + "/enabled?enabled=" + !user.isEnabled())));
    }

    public static void verifyJsonLinksForProfileUser(ResultActions actions, User user) throws Exception{
        actions.andExpect(jsonPath("$._links.self.href", endsWith("/profile")))
                .andExpect(jsonPath("$._links.profile.href", endsWith("/profile")));
    }

    public static void verifyJsonForPageParam(ResultActions actions, int size, int elements, int pages, int number) throws Exception {
        actions.andExpect(jsonPath("$.page.size").value(size))
                .andExpect(jsonPath("$.page.totalElements").value(elements))
                .andExpect(jsonPath("$.page.totalPages").value(pages))
                .andExpect(jsonPath("$.page.number").value(number));
    }
}
