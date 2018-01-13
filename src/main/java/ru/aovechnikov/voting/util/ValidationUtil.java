package ru.aovechnikov.voting.util;

import ru.aovechnikov.voting.HasId;
import ru.aovechnikov.voting.util.exception.NotFoundException;
import ru.aovechnikov.voting.util.exception.TimeExceedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import static ru.aovechnikov.voting.util.DateTimeUtil.TIME_FOR_VOTE;
import static ru.aovechnikov.voting.util.DateTimeUtil.getCurrentDate;

/**
 * Class for data validation
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T entity, int id) {
        checkNotFound(entity, "id=" + id);
        return entity;
    }

    public static <T> T checkNotFound(T entity, String msg){
        checkNotFound(entity != null, msg);
        return entity;
    }

    public static void checkNotFound(boolean found, int id){
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg){
        if (!found){
            throw new NotFoundException( msg);
        }
    }
    /**
     * Checks that {@link HasId} is new, otherwise throws exception {@link IllegalArgumentException }.
     */
    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean + " must be new (id=null)");
        }
    }

    /**
     * Checks that {@link HasId#getId()} equals id  otherwise throws {@link IllegalArgumentException }.
     * if {@link HasId#getId()} is null,  then it is assigned the passed value id.
     */
    public static void checkIdConsistent(HasId bean, int id){
     if (bean.isNew()){
         bean.setId(id);
     }else if (bean.getId() != id){
         throw new IllegalArgumentException(String.format("%s must be with id=%d", bean, id));
     }
    }

    /**
     * Checks that {@code date} equals current date, otherwise throws {@link IllegalArgumentException }.
     *
     * @param date checked date
     */
    public static void checkDateConsistent(LocalDate date){
        if (!Objects.equals(date, getCurrentDate())){
            throw new IllegalArgumentException(String.format("You cannot voting for past menu with date %s", date));
        }
    }

    /**
     * Checks if this {@code time} is before {@link DateTimeUtil#TIME_FOR_VOTE}, otherwise
     * throws {@link TimeExceedException}.
     *
     * @param time
     */
    public static void checkTimeForVote(LocalTime time){
        if (time.isAfter(TIME_FOR_VOTE)){
            throw new TimeExceedException("The voting can only be changed until 11 am");
        }
    }

    /**
     * Finds the root cause of thrown {@link Throwable}.
     * @param t thrown {@link Throwable}
     * @return root {@link Throwable}
     */
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
