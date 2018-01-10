package ru.aovechnikov.voting.service;

import ru.aovechnikov.voting.model.Vote;

import java.time.LocalDate;

public interface VoteService {

    Vote vote(int userId, int menuId);

    Vote getVoteForUserByDate(int userId, LocalDate date);
}
