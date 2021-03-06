package ru.aovechnikov.voting.web.servlet.controllers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.service.UserService;
import ru.aovechnikov.voting.to.UserTo;
import ru.aovechnikov.voting.util.UserUtil;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.aovechnikov.voting.model.Role.ROLE_USER;
import static ru.aovechnikov.voting.testutil.TestUtil.PAGEABLE;
import static ru.aovechnikov.voting.testutil.TestUtil.httpBasic;
import static ru.aovechnikov.voting.testutil.VerifyJsonPathUtil.verifyJsonForUser;
import static ru.aovechnikov.voting.testutil.VerifyJsonPathUtil.verifyJsonLinksForProfileUser;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.*;
import static ru.aovechnikov.voting.util.exception.ErrorType.*;
import static ru.aovechnikov.voting.web.servlet.controllers.ProfileUserController.REST_URL;

/**
 * For testing {@link ProfileUserController}
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public class ProfileUserControllerTest extends AbstractControllerTest{
    @Autowired
    private UserService userService;

    @Test
    public void testGet() throws Exception {
        ResultActions actions = mockMvc.perform(get(REST_URL)
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE));
        verifyJsonForUser(actions, USER1, ROLE_USER.name());
        verifyJsonLinksForProfileUser(actions, USER1);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL)
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print());
            MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER2, ADMIN), userService.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testUpdate() throws Exception {
        User updated = getUpdatedUser();
        UserTo userTo = UserUtil.asTo(updated);
        ResultActions actions = mockMvc.perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userTo))
                .with(httpBasic(USER2)))
                .andExpect(status().isOk())
                .andDo(print());
        MATCHER_FOR_USER.assertEquals(updated, userService.findById(updated.getId()));
        MATCHER_FOR_USER.assertCollectionsEquals(Arrays.asList(USER1, updated, ADMIN), userService.findAll(PAGEABLE).getContent());
    }

    @Test
    public void testUpdateInvalid() throws Exception{
        UserTo updated = new UserTo(USER2.getId(), "", "email@mail.ru", "password");
        mockMvc.perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(USER2)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testDuplicateEmail() throws Exception{
        UserTo updated = new UserTo(USER2.getId(), "updated", USER1.getEmail(), "password");
        mockMvc.perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(USER2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(DATA_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testConsistentIdError() throws Exception{
        UserTo updated = new UserTo(USER2.getId(), "updated", "email@mail.ru", "password");
        mockMvc.perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updated))
                .with(httpBasic(USER1)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value(APP_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testUnAuthentication() throws Exception {
        mockMvc.perform(delete(REST_URL))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.type").value(NOT_AUTHENTICATION.name()))
                .andDo(print());
    }
}