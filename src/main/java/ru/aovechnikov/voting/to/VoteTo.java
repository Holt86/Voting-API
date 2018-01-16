package ru.aovechnikov.voting.to;

import ru.aovechnikov.voting.web.servlet.controllers.VoteController;
import ru.aovechnikov.voting.model.Vote;
import ru.aovechnikov.voting.service.VoteService;


/**
 * Used for the transfer of {@code status} between {@link VoteService} and {@link VoteController}
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class VoteTo {

    private final Vote vote;

    private final boolean created;

    public VoteTo(Vote vote, boolean created) {
        this.vote = vote;
        this.created = created;
    }

    public Vote getVote() {
        return vote;
    }

    public boolean isCreated() {
        return created;
    }
}
