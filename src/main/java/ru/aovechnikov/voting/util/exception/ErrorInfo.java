package ru.aovechnikov.voting.util.exception;

/**
 * DTO for information about errors.
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
public class ErrorInfo {

    private final String url;
    private final ErrorType type;
    private final String typeMessage;
    private final String[] detail;

    public ErrorInfo(CharSequence url, ErrorType type, String typeMessage, String... detail) {
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.detail = detail;
    }
}
