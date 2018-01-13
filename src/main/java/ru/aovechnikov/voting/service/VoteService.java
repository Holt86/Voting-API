package ru.aovechnikov.voting.service;

import ru.aovechnikov.voting.controllers.VoteController;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.model.Vote;
import ru.aovechnikov.voting.to.VoteTo;
import ru.aovechnikov.voting.util.DateTimeUtil;
import ru.aovechnikov.voting.util.exception.TimeExceedException;

import java.time.LocalDate;
/**
 * Service interface for {@link Vote} domain objects.
 * Mostly used as a facade for {@link VoteController}.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
public interface VoteService {

    /**
     * If the vote of the user for the current date doesn't exist in the data store,
     * then creates new {@link Vote} and saves it, returning {@link VoteTo}
     * with {@link VoteTo#created} is {@code true}.
     * If the vote of the user for the current date exists in the data store,
     * then updates it and returns {@link VoteTo} with {@link VoteTo#created} is {@code false}.
     *
     * @param userId id of the voting user
     * @param menuId the menu id for which to vote
     * @return {@link VoteTo}
     * @throws IllegalArgumentException if the date of menu isn't the current date
     * @throws TimeExceedException if current time is after {@link DateTimeUtil#TIME_FOR_VOTE}
     * when updated an existing vote.
     */
    VoteTo vote(int userId, int menuId);

    /**
     * Retrieve {@link Vote} with {@link Menu}from the data store
     * by id of {@link User} and {@link LocalDate}.
     *
     * @param userId id of {@link User}
     * @param date
     * @return {@link Vote}
     */
    Vote getVoteForUserByDate(int userId, LocalDate date);

}
