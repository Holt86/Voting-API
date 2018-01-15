package ru.aovechnikov.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;

/**
 *DTO used to map a projection query from data store. Contains statistics about voting.
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
@JsonPropertyOrder({"countVotes","nameRestaurant", "id"})
public class ResultTo extends HasIdTo {

    private String nameRestaurant;

    private LocalDate date;

    private int countVotes;

    public ResultTo() {
    }

    @Override
    @JsonProperty(value = "menuId")
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    public ResultTo(Integer id, String nameRestaurant, LocalDate date, int countVotes) {
        super(id);
        this.nameRestaurant = nameRestaurant;
        this.date = date;
        this.countVotes = countVotes;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public long getCountVotes() {
        return countVotes;
    }

    public void setCountVotes(int countVotes) {
        this.countVotes = countVotes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ResultTo{" +
                "id='" + id + '\'' +
                "name='" + nameRestaurant + '\'' +
                ", countVotes=" + countVotes + '\'' +
                ", date=" + date +
                '}';
    }
}