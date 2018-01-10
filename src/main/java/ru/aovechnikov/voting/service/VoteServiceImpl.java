package ru.aovechnikov.voting.service;

import org.springframework.stereotype.Service;
import ru.aovechnikov.voting.model.Vote;

import java.time.LocalDate;

@Service
public class VoteServiceImpl implements VoteService{


    @Override
    public Vote vote(int userId, int menuId) {
        return null;
    }

    @Override
    public Vote getVoteForUserByDate(int userId, LocalDate date) {
        return null;
    }
}
