package ru.aovechnikov.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.Vote;
import ru.aovechnikov.voting.repository.UserRepository;
import ru.aovechnikov.voting.repository.VoteRepository;
import ru.aovechnikov.voting.to.VoteTo;

import java.time.LocalDate;

import static ru.aovechnikov.voting.util.DateTimeUtil.getCurrentDate;
import static ru.aovechnikov.voting.util.DateTimeUtil.getCurrentTime;
import static ru.aovechnikov.voting.util.ValidationUtil.checkDateConsistent;
import static ru.aovechnikov.voting.util.ValidationUtil.checkNotFound;
import static ru.aovechnikov.voting.util.ValidationUtil.checkTimeForVote;

/**
 * Implementation of the {@link VoteService}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

@Service
public class VoteServiceImpl implements VoteService{

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuService menuService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public VoteTo vote(int userId, int menuId) {
        Menu menu = menuService.findById(menuId);
        checkDateConsistent(menu.getDate());
        Vote vote;
        if ((vote = voteRepository.getVoteForUserByDate(userId, getCurrentDate())) == null){
            return new VoteTo(voteRepository.save(new Vote(null, getCurrentDate(),
                    userRepository.getOne(userId), menu)), true);
        }else {
            checkTimeForVote(getCurrentTime());
            vote.setMenu(menu);
            return new VoteTo(voteRepository.save(vote), false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vote getVoteForUserByDate(int userId, LocalDate date) {
        Assert.notNull(date, "date must be not null");
        return checkNotFound(voteRepository.getVoteWithMenuForUserByDate(userId, date), String.format("Vote for user with id=%s by datetime %s", userId, date));
    }
}
