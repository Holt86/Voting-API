package ru.aovechnikov.voting.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Utility class for working with date and time in the app
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class DateTimeUtil {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static LocalDateTime currentDateTime = LocalDateTime.now();

    /**
     * Final time for voting
     */
    public static final LocalTime TIME_FOR_VOTE = LocalTime.of(11, 00);

    private DateTimeUtil() {
    }

    public static LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    /**
     * Sets the {@code currentDateTime} as the current date and time in the app.
     * Used for easy testing of the app.
     * @param currentDateTime
     */
    public static void setCurrentDateTime(LocalDateTime currentDateTime) {
        DateTimeUtil.currentDateTime = currentDateTime;
    }

    /**
     * Returns the current date.
     *
     * @return current date
     */
    public static LocalDate getCurrentDate(){
     return getCurrentDateTime().toLocalDate();
    }

    /**
     * Returns the current time.
     *
     * @return current time
     */
    public static LocalTime getCurrentTime(){
        return getCurrentDateTime().toLocalTime();
    }

    /**
     * if {@code date} is null returns the current date, otherwise {@code date}.
     *
     * @param date date to check
     * @return checked date
     */
    public static LocalDate getCurrentDateIfNull(LocalDate date){
        return date == null ? getCurrentDate() : date;
    }
}
