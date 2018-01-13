package ru.aovechnikov.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.aovechnikov.voting.model.Menu;
import ru.aovechnikov.voting.model.User;
import ru.aovechnikov.voting.model.Vote;
import ru.aovechnikov.voting.to.ResultTo;

import javax.persistence.SqlResultSetMapping;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository class for {@link Vote} domain objects.
 * @author - A.Ovechnikov
 * @date - 11.01.2018
 */
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    /**
     * Retrieve {@link Vote} with {@link Menu} from the data store
     * for {@link User} by {@link LocalDate}.
     *
     * @param userId id of {@link User}
     * @param date Value to search for
     * @return {@link Vote} with {@link Menu} or {@literal null} if none found.
     */
    @Query("SELECT v FROM Vote v JOIN FETCH v.menu WHERE v.user.id=:userId AND v.date=:date")
    Vote getVoteWithMenuForUserByDate(@Param("userId") int userId, @Param("date") LocalDate date);

    /**
     * Retrieve {@link Vote} from the data store for {@link User}
     * by {@link LocalDate}.
     *
     * @param userId id of {@link User}
     * @param date Value to search for
     * @return {@link Vote} or {@literal null} if none found.
     */
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.date=:date")
    Vote getVoteForUserByDate(@Param("userId") int userId, @Param("date") LocalDate date);

    /**
     * Retrieve {@link List} of {@link ResultTo} from the data store
     * by {@link LocalDate} uses a native SQL query with {@link SqlResultSetMapping}.
     *
     * @param date Value to search for
     * @return {@link List} of {@link ResultTo}
     */
    List<ResultTo> getAllResultToByDate(@Param("date") LocalDate date);
}
