package ru.aovechnikov.voting.web.servlet.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.aovechnikov.voting.model.Vote;
import ru.aovechnikov.voting.service.VoteService;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.aovechnikov.voting.testutil.TestUtil.httpBasic;
import static ru.aovechnikov.voting.testutil.VerifyJsonPathUtil.verifyJsonForVote;
import static ru.aovechnikov.voting.testutil.testdata.DateTestData.*;
import static ru.aovechnikov.voting.testutil.testdata.MenuTestData.MENU_1;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.ID_NOT_FOUND;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.USER1;
import static ru.aovechnikov.voting.testutil.testdata.VoteTestData.*;
import static ru.aovechnikov.voting.util.DateTimeUtil.setCurrentDateTime;
import static ru.aovechnikov.voting.util.exception.ErrorType.*;
import static ru.aovechnikov.voting.web.servlet.controllers.VoteController.REST_URL;

/**
 * For testing {@link VoteController}
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public class VoteControllerTest extends AbstractControllerTest {

    private static final String URL_TEST = REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @Before
    public void setDateTime() {
        setCurrentDateTime(LocalDateTime.now());
    }

    @Test
    public void testGetVoteForUserByDate() throws Exception {
        ResultActions actions = mockMvc.perform(get(URL_TEST + "?date=" + VOTE1.getDate())
                .with(httpBasic(VOTE1.getUser())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForVote(actions, VOTE1);
    }

    @Test
    public void testVote() throws Exception {
        setCurrentDateTime(DATE_TIME2_AFTER);
        Vote created = getCreatedVote();
        ResultActions actions = mockMvc.perform(post(URL_TEST + created.getMenu().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic(created.getUser())))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForVote(actions, created);
        Vote voteFromDb = voteService.getVoteForUserByDate(created.getUser().getId(), created.getDate());
        created.setId(voteFromDb.getId());
        MATCHER_FOR_VOTE.assertEquals(created, voteFromDb);
    }

    @Test
    public void testUpdateVoteBeforeFinalTime() throws Exception {
        setCurrentDateTime(DATE_TIME2_BEFORE);
        Vote updated = getUpdatedVote();
        ResultActions actions = mockMvc.perform(post(URL_TEST + updated.getMenu().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic(updated.getUser())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andDo(print());
        verifyJsonForVote(actions, updated);
        MATCHER_FOR_VOTE.assertEquals(updated, voteService.getVoteForUserByDate(updated.getUser().getId(), updated.getDate()));
    }

    @Test
    public void testUpdateVoteAfterFinalTime() throws Exception {
        setCurrentDateTime(DATE_TIME2_AFTER);
        Vote updated = getUpdatedVote();
        mockMvc.perform(post(URL_TEST + updated.getMenu().getId())
                .with(httpBasic(updated.getUser())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(DATA_LATE_UPDATE.name()))
                .andDo(print());
    }

    @Test
    public void testGetVoteForUserByDateNotFound() throws Exception {
        mockMvc.perform(get(URL_TEST + "?date=2017-12-26")
                .with(httpBasic(VOTE1.getUser())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(DATA_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    public void testVoteNotFoundMenu() throws Exception {
        Vote updated = getUpdatedVote();
        mockMvc.perform(post(URL_TEST + ID_NOT_FOUND)
                .with(httpBasic(updated.getUser())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(DATA_NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    public void testVoteForOldMenu() throws Exception {
        Vote updated = getUpdatedVote();
        updated.setMenu(MENU_1);
        mockMvc.perform(post(URL_TEST + updated.getMenu().getId())
                .with(httpBasic(updated.getUser())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value(APP_ERROR.name()))
                .andDo(print());
    }

    @Test
    public void testGetResultVotesByDate() throws Exception {
        mockMvc.perform(get(URL_TEST + "result?date=" + DATE_1)
                .with(httpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].countVotes").value(RESULT_TO_MENU_1.getCountVotes()))
                .andExpect(jsonPath("$[0].nameRestaurant").value(RESULT_TO_MENU_1.getNameRestaurant()))
                .andExpect(jsonPath("$[0].menuId").value(RESULT_TO_MENU_1.getId()))
                .andExpect(jsonPath("$[0].date").value(RESULT_TO_MENU_1.getDate().toString()))
                .andExpect(jsonPath("$[0]._links.menu.href", endsWith("/menus/" + RESULT_TO_MENU_1.getId())))
                .andExpect(jsonPath("$[0]._links.restaurant.href", endsWith("/menus/" + RESULT_TO_MENU_1.getId() + "/restaurant")))
                .andExpect(jsonPath("$[1].countVotes").value(RESULT_TO_MENU_2.getCountVotes()))
                .andExpect(jsonPath("$[1].nameRestaurant").value(RESULT_TO_MENU_2.getNameRestaurant()))
                .andExpect(jsonPath("$[1].menuId").value(RESULT_TO_MENU_2.getId()))
                .andExpect(jsonPath("$[1].date").value(RESULT_TO_MENU_2.getDate().toString()))
                .andExpect(jsonPath("$[1]._links.menu.href", endsWith("/menus/" + RESULT_TO_MENU_2.getId())))
                .andExpect(jsonPath("$[1]._links.restaurant.href", endsWith("/menus/" + RESULT_TO_MENU_2.getId() + "/restaurant")));
    }

    @Test
    public void testUnAuthentication() throws Exception {
        setCurrentDateTime(DATE_TIME2_AFTER);
        Vote created = getCreatedVote();
        mockMvc.perform(post(URL_TEST + created.getMenu().getId()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.type").value(NOT_AUTHENTICATION.name()))
                .andDo(print());
    }
}