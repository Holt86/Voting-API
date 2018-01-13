package ru.aovechnikov.voting.util.exception;

/**
 * Thrown if entity not found into data store.
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
