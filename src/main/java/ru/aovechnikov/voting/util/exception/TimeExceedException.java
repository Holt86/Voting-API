package ru.aovechnikov.voting.util.exception;

import ru.aovechnikov.voting.util.DateTimeUtil;

/**
 * Thrown if the current time exceeds the {@link DateTimeUtil#currentDateTime} for the updated vote.
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */
public class TimeExceedException extends RuntimeException{

    public TimeExceedException(String message) {
        super(message);
    }
}
