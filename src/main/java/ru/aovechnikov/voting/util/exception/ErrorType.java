package ru.aovechnikov.voting.util.exception;

/**
 * Enum for type error.
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public enum ErrorType {

    DATA_LATE_UPDATE("error.dataLateUpdate"),
    DATA_NOT_FOUND("error.dataNotFound"),
    VALIDATION_ERROR("error.validationError"),
    DATA_ERROR("error.dataError"),
    APP_ERROR("error.appError"),
    ACCESS_DENIED("error.accessDenied"),
    NOT_AUTHENTICATION("error.authentication");


    private final String errorCode;

    ErrorType(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
