package ru.aovechnikov.voting.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aovechnikov.voting.model.Vote;
import ru.aovechnikov.voting.to.VoteTo;
import ru.aovechnikov.voting.util.DateTimeUtil;
import ru.aovechnikov.voting.util.exception.NotFoundException;
import ru.aovechnikov.voting.util.exception.TimeExceedException;

import java.util.Arrays;

import static ru.aovechnikov.voting.testutil.testdata.DateTestData.*;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.USER1_ID;
import static ru.aovechnikov.voting.testutil.testdata.UserTestData.USER2_ID;
import static ru.aovechnikov.voting.testutil.testdata.VoteTestData.*;
/**
 * For testing {@link VoteService}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class VoteServiceImplTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Test
    public void testGetVoteForUserByDate() throws Exception {
        DateTimeUtil.setCurrentDateTime(DATE_TIME2_BEFORE);
        MATCHER_FOR_VOTE.assertEquals(VOTE1, service.getVoteForUserByDate(USER2_ID, DATE_2));
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundVoteForUserByDate() throws Exception {
        MATCHER_FOR_VOTE.assertEquals(VOTE1, service.getVoteForUserByDate(USER1_ID, DATE_2));
    }

    @Test
    public void testVote() throws Exception {
        DateTimeUtil.setCurrentDateTime(DATE_TIME2_AFTER);
        Vote created = getCreatedVote();
        VoteTo returned = service.vote(created.getUser().getId(), created.getMenu().getId());
        created.setId(returned.getVote().getId());

        Assert.assertEquals(true, returned.isCreated());
        MATCHER_FOR_VOTE.assertEquals(created, service.getVoteForUserByDate(USER1_ID, DATE_2));
    }

    @Test
    public void testVoteBeforeFinalTime() throws Exception {
        DateTimeUtil.setCurrentDateTime(DATE_TIME2_BEFORE);
        Vote updated = getUpdatedVote();
        VoteTo returned = service.vote(updated.getUser().getId(), updated.getMenu().getId());

        Assert.assertEquals(false, returned.isCreated());
        MATCHER_FOR_VOTE.assertEquals(updated, service.getVoteForUserByDate(USER2_ID, DATE_2));
    }

    @Test(expected = TimeExceedException.class)
    public void testVoteAfterFinalTime() throws Exception {
        DateTimeUtil.setCurrentDateTime(DATE_TIME2_AFTER);
        Vote updated = getUpdatedVote();
        service.vote(updated.getUser().getId(), updated.getMenu().getId());
    }

    @Test
    public void testGetAllResultToByDate() throws Exception {
        MATCHER_FOR_RESULT_TO.assertCollectionsEquals(Arrays.asList(RESULT_TO_MENU_1, RESULT_TO_MENU_2), service.getAllResultToByDate(DATE_1));
    }
}