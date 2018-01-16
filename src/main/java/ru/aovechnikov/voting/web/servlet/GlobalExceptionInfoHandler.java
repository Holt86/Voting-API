package ru.aovechnikov.voting.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.aovechnikov.voting.util.exception.ErrorInfo;
import ru.aovechnikov.voting.util.exception.ErrorType;
import ru.aovechnikov.voting.util.exception.NotFoundException;
import ru.aovechnikov.voting.util.exception.TimeExceedException;
import ru.aovechnikov.voting.util.message.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.aovechnikov.voting.util.ValidationUtil.getRootCause;
import static ru.aovechnikov.voting.util.exception.ErrorType.*;

/**
 * Controller advice for exception handling.
 *
 * @author - A.Ovechnikov
 * @date - 15.01.2018
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionInfoHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionInfoHandler.class);

    public static final String EXCEPTION_TIME_EXCEED = "exception.vote.exceedTime";
    public static final String EXCEPTION_NOT_FOUND = "exception.common.notFound";

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_NAME_RESTAURANT = "exception.restaurant.duplicateName";
    public static final String EXCEPTION_DUPLICATE_MENU_FOR_RESTAURANT = "exception.menu.duplicateMenu";
    public static final String EXCEPTION_DUPLICATE_NAME_DISH_FOR_MENU = "exception.dish.duplicateName";


    public static final String EXCEPTION_CONSTRAIN_FK_FOR_MENU = "exception.menu.constrainForeignKey";
    public static final String EXCEPTION_CONSTRAIN_FK_FOR_DISH = "exception.dish.constrainForeignKey";

    private static final Map<String, String> CONSTRAINS_I18N_MAP = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL);
                    put("restaurant_unique_name_idx", EXCEPTION_DUPLICATE_NAME_RESTAURANT);
                    put("date_name_restaurant_idx", EXCEPTION_DUPLICATE_MENU_FOR_RESTAURANT);
                    put("date_name_menu_idx", EXCEPTION_DUPLICATE_NAME_DISH_FOR_MENU);
                    put("fk_menu_to_restaurant_id", EXCEPTION_CONSTRAIN_FK_FOR_MENU);
                    put("fk_dish_to_menu_id", EXCEPTION_CONSTRAIN_FK_FOR_DISH);
                }
            });

    @Autowired
    private MessageUtil messageUtil;

    /**
     * Handles {@link NotFoundException}.
     *
     * @param req request information
     * @param e  throws exception
     * @return {@link ErrorInfo} with {@link HttpStatus#NOT_FOUND}
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND, messageUtil.getMessage(EXCEPTION_NOT_FOUND, new Object[]{e.getMessage()}));
    }

    /**
     * Handles {@link MethodArgumentNotValidException}.
     *
     * @param req request information
     * @param e  throws exception
     * @return {@link ErrorInfo} with {@link HttpStatus#UNPROCESSABLE_ENTITY}
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo bindValidationError(HttpServletRequest req, MethodArgumentNotValidException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream().map(fe -> messageUtil.getMessage(fe)).toArray(String[]::new);
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, details);
    }

    /**
     * Handles {@link DataIntegrityViolationException} and child.
     *
     * @param req request information
     * @param e  throws exception
     * @return {@link ErrorInfo} with {@link HttpStatus#CONFLICT}
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            Optional<Map.Entry<String, String>> entrySet = CONSTRAINS_I18N_MAP.entrySet().stream().filter(entry -> lowerCaseMsg.contains(entry.getKey())).findAny();
            return logAndGetErrorInfo(req, e, true, DATA_ERROR, entrySet.isPresent() ? messageUtil.getMessage(entrySet.get().getValue(), null) : rootMsg);
        } else {
            return logAndGetErrorInfo(req, e, true, DATA_ERROR);
        }
    }

    /**
     * Handles {@link TimeExceedException}.
     *
     * @param req request information
     * @param e  throws exception
     * @return {@link ErrorInfo} with {@link HttpStatus#CONFLICT}
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(TimeExceedException.class)
    public ErrorInfo timeExceedError(HttpServletRequest req, TimeExceedException e) {
        return logAndGetErrorInfo(req, e, false, DATA_LATE_UPDATE, messageUtil.getMessage(EXCEPTION_TIME_EXCEED, null));
    }

    /**
     * Handles {@link AccessDeniedException} and child.
     *
     * @param req request information
     * @param e  throws exception
     * @return {@link ErrorInfo} with {@link HttpStatus#FORBIDDEN}
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorInfo handleAccessDenied(HttpServletRequest req, Exception e) {
        ErrorInfo errorInfo = logAndGetErrorInfo(req, e, false, ACCESS_DENIED, e.getMessage());
        return errorInfo;
    }

    /**
     * Handles {@link Exception} and child.
     *
     * @param req request information
     * @param e  throws exception
     * @return {@link ErrorInfo} with {@link HttpStatus#FORBIDDEN}
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR, e.getMessage());
    }

    /**
     * Login root case of {@code e}, returns filled {@link ErrorInfo}.
     *
     * @param req request information
     * @param e throws exception
     * @param logException status logging
     * @param errorType {@link ErrorType} type of error
     * @param details detail of exception
     * @return
     */
    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String... details) {
        Throwable rootCause = getRootCause(e);
        if (logException) {
            log.error(errorType + "in request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} in request {}, {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), errorType, messageUtil.getMessage(errorType.getErrorCode(), null),
                details.length != 0 ? details : new String[]{rootCause.toString()});
    }
}
