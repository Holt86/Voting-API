package ru.aovechnikov.voting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.aovechnikov.voting.to.ResultTo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Simple entity representing an voting.
 * @author - A.Ovechnikov
 * @date - 09.01.2018
 */
@SqlResultSetMapping(
        name = "ResultToMapping",
        classes = @ConstructorResult(
                targetClass = ResultTo.class,
                columns = {@ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "nameRestaurant", type = String.class),
                        @ColumnResult(name = "date", type = LocalDate.class),
                        @ColumnResult(name = "countVotes", type = Integer.class)}))
@NamedNativeQueries({
        @NamedNativeQuery(name = Vote.GET_COUNT_VOTES_FOR_MENU_BY_DATE, query =
                "SELECT m.id, r.name AS nameRestaurant, m.date, count(v.id) AS countVotes FROM menu m LEFT JOIN user_voter v ON m.id=v.menu_id " +
                        "LEFT JOIN restaurant r ON m.restaurant_id=r.id WHERE m.date=:date GROUP BY m.id, r.name ORDER BY countVotes DESC",
                resultSetMapping = "ResultToMapping")
})

@Entity
@Table(name = "user_voter",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_vote"}, name = "user_id_date_vote_idx")})
public class Vote extends AbstractBaseEntity {

    public static final String GET_COUNT_VOTES_FOR_MENU_BY_DATE = "Vote.getAllResultToByDate";

    @Column(name = "date_vote", nullable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Menu menu;

    public Vote() {
    }

    public Vote(Integer id, LocalDate date, User user, Menu menu) {
        super(id);
        this.date = date;
        this.user = user;
        this.menu = menu;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + getId() +
                ", date=" + date +
                ", user=" + user +
                ", menu=" + menu +
                '}';
    }
}
