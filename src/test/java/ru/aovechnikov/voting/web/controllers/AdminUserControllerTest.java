package ru.aovechnikov.voting.web.controllers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.Role;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.service.UserService;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aovechnikov.voting.model.Role.ROLE_ADMIN;
import static ru.aovechnikov.voting.model.Role.ROLE_USER;
import static ru.aovechnikov.voting.testutil.TestUtil.*;
import static ru.aovechnikov.voting.testutil.VerifyJsonPathUtil.*;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.*;
import static ru.aovechnikov.voting.util.exception.ErrorType.*;
import static ru.aovechnikov.voting.web.controllers.AdminUserController.REST_URL;

/**
 * For testing {@link AdminUserController}
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public class AdminUserControllerTest extends AbstractControllerTest{

    private static final String URL_TEST = REST_URL + '/';

    @Autowired
    private UserService userService;

    @Test
    public void testFindById() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + USER1_ID)
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE));
        verifyJsonForUser(actions, USER1, ROLE_USER.name());
        verifyJsonLinksForAdminUser(actions, USER1);

    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL_TEST + USER1_ID)
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print());
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER2, ADMIN), userService.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testUpdateExist() throws Exception {
        User updated = getUpdatedUser();
        ResultActions actions = mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForUser(actions, updated, ROLE_USER.name());
        verifyJsonLinksForAdminUser(actions, updated);
        MATCHER_FOR_USER.assertEquals(updated, userService.findById(updated.getId()));
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER1, updated, ADMIN), userService.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testUpdateNotExist() throws Exception {
        User updated = getUpdatedUser();
        updated.setId(ID_NOT_FOUND);
        ResultActions actions = mockMvc.perform(put(URL_TEST + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        updated.setId(getIdFromLink(actions, "$._links.self.href"));
        verifyJsonForUser(actions, updated, ROLE_USER.name());
        verifyJsonLinksForAdminUser(actions, updated);
        MATCHER_FOR_USER.assertEquals(updated, userService.findById(updated.getId()));
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER1, USER2, ADMIN, updated), userService.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testCreate() throws Exception {
        User created = getCreatedUser();
        ResultActions actions = mockMvc.perform(post(URL_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(created))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        created.setId(getIdFromLink(actions, "$._links.self.href"));
        verifyJsonForUser(actions, created, ROLE_USER.name());
        verifyJsonLinksForAdminUser(actions, created);

        MATCHER_FOR_USER.assertEquals(created, userService.findById(created.getId()));
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER1, USER2, ADMIN, created), userService.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testFindByEmail() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + "by-email?email=" + USER1.getEmail())
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE));
        verifyJsonForUser(actions, USER1, ROLE_USER.name());
        verifyJsonLinksForAdminUser(actions, USER1);
    }

    @Test
    public void testFindAll() throws Exception {
        ResultActions actions = mockMvc.perform(get(REST_URL + "?page=0&size=3&sort=id")
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._embedded.userResourceList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.userResourceList[0].name").value(USER1.getName()))
                .andExpect(jsonPath("$._embedded.userResourceList[0].email").value(USER1.getEmail()))
                .andExpect(jsonPath("$._embedded.userResourceList[0].enabled").value(USER1.isEnabled()))
                .andExpect(jsonPath("$._embedded.userResourceList[0].roles", hasSize(USER1.getRoles().size())))
                .andExpect(jsonPath("$._embedded.userResourceList[0].roles").value(containsInAnyOrder(ROLE_USER.name())))
                .andExpect(jsonPath("$._embedded.userResourceList[0]._links.self.href", endsWith("/users/" + USER1.getId())))
                .andExpect(jsonPath("$._embedded.userResourceList[0]._links.user.href", endsWith("/users/" + USER1.getId())))
                .andExpect(jsonPath("$._embedded.userResourceList[0]._links.enabled.href", endsWith("/users/" + USER1.getId() + "/enabled?enabled=" + !USER1.isEnabled())))
                .andExpect(jsonPath("$._embedded.userResourceList[1].name").value(USER2.getName()))
                .andExpect(jsonPath("$._embedded.userResourceList[1].email").value(USER2.getEmail()))
                .andExpect(jsonPath("$._embedded.userResourceList[1].enabled").value(USER2.isEnabled()))
                .andExpect(jsonPath("$._embedded.userResourceList[1].roles", hasSize(USER2.getRoles().size())))
                .andExpect(jsonPath("$._embedded.userResourceList[1].roles").value(containsInAnyOrder(ROLE_USER.name())))
                .andExpect(jsonPath("$._embedded.userResourceList[1]._links.self.href", endsWith("/users/" + USER2.getId())))
                .andExpect(jsonPath("$._embedded.userResourceList[1]._links.user.href", endsWith("/users/" + USER2.getId())))
                .andExpect(jsonPath("$._embedded.userResourceList[1]._links.enabled.href", endsWith("/users/" + USER2.getId() + "/enabled?enabled=" + !USER2.isEnabled())))
                .andExpect(jsonPath("$._embedded.userResourceList[2].name").value(ADMIN.getName()))
                .andExpect(jsonPath("$._embedded.userResourceList[2].email").value(ADMIN.getEmail()))
                .andExpect(jsonPath("$._embedded.userResourceList[2].enabled").value(ADMIN.isEnabled()))
                .andExpect(jsonPath("$._embedded.userResourceList[2].roles", hasSize(ADMIN.getRoles().size())))
                .andExpect(jsonPath("$._embedded.userResourceList[2].roles").value(containsInAnyOrder(ROLE_USER.name(), ROLE_ADMIN.name())))
                .andExpect(jsonPath("$._embedded.userResourceList[2]._links.self.href", endsWith("/users/" + ADMIN.getId())))
                .andExpect(jsonPath("$._embedded.userResourceList[2]._links.user.href", endsWith("/users/" + ADMIN.getId())))
                .andExpect(jsonPath("$._embedded.userResourceList[2]._links.enabled.href", endsWith("/users/" + ADMIN.getId() + "/enabled?enabled=" + !ADMIN.isEnabled())));

        verifyJsonForPageParam(actions, 3, 3, 1, 0);
    }

    @Test
    public void testEnable() throws Exception {
        User disable = new User(USER1);
        disable.setEnabled(false);
        ResultActions actions = mockMvc.perform(put(URL_TEST + disable.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(disable))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForUser(actions, disable, ROLE_USER.name());
        verifyJsonLinksForAdminUser(actions, disable);
        MATCHER_FOR_USER.assertEquals(disable, userService.findById(disable.getId()));
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get(URL_TEST + ID_NOT_FOUND)
                .with(httpBasic(ADMIN)))
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
    public void testCreateInvalid() throws Exception{
        User expected = new User(null, null, "", "newPass",  Role.ROLE_USER, Role.ROLE_ADMIN);
        mockMvc.perform(post(URL_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(expected))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testDuplicateEmail() throws Exception{
        User expected = new User(null, "newUser", "user1@yandex.ru", "newPass",  Role.ROLE_USER, Role.ROLE_ADMIN);
        mockMvc.perform(post(URL_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(expected))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(DATA_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testConsistentIdError() throws Exception{
        User expected = new User(100, "newUser", "user1@yandex.ru", "newPass",  Role.ROLE_USER, Role.ROLE_ADMIN);
        mockMvc.perform(put(URL_TEST + USER2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(expected))
                .with(httpBasic(ADMIN)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value(APP_ERROR.name()))
                .andDo(print());
    }
}